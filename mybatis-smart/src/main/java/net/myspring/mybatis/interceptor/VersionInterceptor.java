/**
 * The MIT License (MIT)
 *
 * Copyright (c) 2016 342252328@qq.com
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package net.myspring.mybatis.interceptor;

import net.myspring.mybatis.context.ProviderContextUtils;
import net.myspring.mybatis.dto.TableDto;
import net.myspring.mybatis.exception.StaleObjectStateException;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.executor.parameter.ParameterHandler;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.ParameterMapping;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.SystemMetaObject;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.type.TypeException;
import org.apache.ibatis.type.TypeHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.Entity;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Proxy;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Properties;

@Intercepts({
        @Signature(type = StatementHandler.class, method = "prepare", args = {Connection.class,Integer.class}),
        @Signature(type = ParameterHandler.class, method = "setParameters", args = {PreparedStatement.class}),
        @Signature(type = Executor.class, method = "update", args = {MappedStatement.class,Object.class})
})
public class VersionInterceptor implements Interceptor {

    private static Logger logger = LoggerFactory.getLogger(VersionInterceptor.class);

    public Object intercept(Invocation invocation) throws Exception {
        String interceptMethod = invocation.getMethod().getName();
        if("prepare".equals(interceptMethod)) {
            StatementHandler statementHandler = (StatementHandler) processTarget(invocation.getTarget());
            MetaObject metaObject = SystemMetaObject.forObject(statementHandler);
            MappedStatement mappedStatement = (MappedStatement) metaObject.getValue("delegate.mappedStatement");
            SqlCommandType sqlCommandType = mappedStatement.getSqlCommandType();
            if(sqlCommandType != SqlCommandType.UPDATE) {
                return invocation.proceed();
            }
            BoundSql boundSql = (BoundSql) metaObject.getValue("delegate.boundSql");
            if(!checkVersion(boundSql)) {
                return invocation.proceed();
            }
            TableDto tableDto = ProviderContextUtils.getTableDto(boundSql.getParameterObject().getClass());
            String versionColumn =tableDto.getVersionColumn().getJdbcColumn();
            String originalSql = (String) metaObject.getValue("delegate.boundSql.sql");
            StringBuilder builder = new StringBuilder(originalSql);
            builder.append(" and ");
            builder.append(versionColumn);
            builder.append(" = ?");
            String replaceSql = StringUtils.replacePattern(builder.toString(),"\\s+(?i)set\\s+"," SET "+versionColumn + " = " + versionColumn + " + 1,");
            metaObject.setValue("delegate.boundSql.sql", replaceSql);
            return invocation.proceed();
        } else if("setParameters".equals(interceptMethod)) {
            ParameterHandler parameterHandler = (ParameterHandler) processTarget(invocation.getTarget());
            MetaObject metaObject = SystemMetaObject.forObject(parameterHandler);
            MappedStatement mappedStatement = (MappedStatement) metaObject.getValue("mappedStatement");
            SqlCommandType sqlCommandType = mappedStatement.getSqlCommandType();
            if(sqlCommandType != SqlCommandType.UPDATE) {
                return invocation.proceed();
            }
            Configuration configuration = mappedStatement.getConfiguration();
            BoundSql boundSql = (BoundSql) metaObject.getValue("boundSql");
            if(!checkVersion(boundSql)) {
                return invocation.proceed();
            }
            TableDto tableDto = ProviderContextUtils.getTableDto(boundSql.getParameterObject().getClass());
            String versionColumn = tableDto.getVersionColumn().getJdbcColumn();
            ParameterMapping parameterMapping = new ParameterMapping.Builder(configuration, versionColumn, Long.class).build();
            TypeHandler typeHandler = parameterMapping.getTypeHandler();

            String tableName = tableDto.getJdbcTable();
            StringBuilder sb = new StringBuilder();
            sb.append("SELECT ").append(versionColumn).append(" FROM ").append(tableName).append(" WHERE ").append(tableDto.getIdColumn().getJdbcColumn()).append(" = ").append("?");
            String versionSql = sb.toString();

            Long currentVersion = getCurrentVersion(versionSql,mappedStatement,boundSql,versionColumn);
            List<ParameterMapping> parameterMappings = boundSql.getParameterMappings();
            try {
                PreparedStatement ps = (PreparedStatement) invocation.getArgs()[0];
                typeHandler.setParameter(ps, parameterMappings.size() + 1, currentVersion,parameterMapping.getJdbcType());
            } catch (TypeException e) {
                throw new TypeException("Could not set parameters for mapping: " + parameterMappings + ". Cause: " + e, e);
            } catch (SQLException e) {
                throw new TypeException("Could not set parameters for mapping: " + parameterMappings + ". Cause: " + e, e);
            }
            return invocation.proceed();
        } else if("update".equals(interceptMethod)) {
            Object result = invocation.proceed();
            Object[] queryArgs = invocation.getArgs();
            MappedStatement mappedStatement = (MappedStatement) queryArgs[0];
            SqlCommandType sqlCommandType = mappedStatement.getSqlCommandType();
            if(sqlCommandType != SqlCommandType.UPDATE) {
                return result;
            }
            BoundSql boundSql = mappedStatement.getBoundSql(queryArgs[1]);
            if(!checkVersion(boundSql)) {
                return result;
            }
            Object paramObj = boundSql.getParameterObject();
            TableDto tableDto = ProviderContextUtils.getTableDto(paramObj.getClass());
            String versionColumn = tableDto.getVersionColumn().getJdbcColumn();
            if(StringUtils.isNotBlank(versionColumn)) {
                if(Integer.valueOf(result.toString())==0) {
                    Field field = paramObj.getClass().getDeclaredField(tableDto.getIdColumn().getJavaInstance());
                    field.setAccessible(true);
                    String id = (String) field.get(paramObj);
                    throw new StaleObjectStateException(paramObj.getClass().getName(),id);
                }
            }
            return result;
        }
        return invocation.proceed();
    }

    @Override
    public Object plugin(Object target) {
        if (target instanceof StatementHandler || target instanceof ParameterHandler || target instanceof  Executor) {
            return Plugin.wrap(target, this);
        } else {
            return target;
        }
    }

    @Override
    public void setProperties(Properties properties) {

    }

    private Boolean checkVersion(BoundSql boundSql) {
        Object paramObj = boundSql.getParameterObject();
        if(paramObj.getClass().getAnnotation(Entity.class)==null) {
            return false;
        }
        TableDto tableDto = ProviderContextUtils.getTableDto(paramObj.getClass());
       String sql = boundSql.getSql();
        if(!sql.matches("[\\s\\S]*\\s+(\\w+\\.)?(?i)" + tableDto.getIdColumn().getJdbcColumn() + "\\s*=\\s*\\?[\\s\\S]*")) {
            return false;
        }
        if(tableDto == null || tableDto.getVersionColumn() == null || tableDto.getIdColumn() == null) {
            return false;
        }
        return true;
    }

    /**
     * <p>Recursive get the original target object.
     * <p>If integrate more than a plugin, maybe there are conflict in these plugins, because plugin will proxy the object.<br>
     * So, here get the orignal target object
     *
     * @param target proxy-object
     * @return original target object
     */
    private Object processTarget(Object target) {
        if(Proxy.isProxyClass(target.getClass())) {
            MetaObject mo = SystemMetaObject.forObject(target);
            return processTarget(mo.getValue("h.target"));
        }
        return target;
    }


    private Long getCurrentVersion(String sql, MappedStatement mappedStatement,BoundSql boundSql,String versionColumn) throws SQLException, NoSuchMethodException, InvocationTargetException, IllegalAccessException, NoSuchFieldException {
        Connection connection = null;
        PreparedStatement versionStmt = null;
        ResultSet rs = null;
        try {
            Object paramObj = boundSql.getParameterObject();
            Field field = paramObj.getClass().getDeclaredField(ProviderContextUtils.getTableDto(paramObj.getClass()).getIdColumn().getJavaInstance());
            field.setAccessible(true);
            String id = (String) field.get(paramObj);
            connection = mappedStatement.getConfiguration().getEnvironment().getDataSource().getConnection();
            versionStmt = connection.prepareStatement(sql);
            ParameterMapping parameterMapping = new ParameterMapping.Builder(mappedStatement.getConfiguration(), versionColumn, String.class).build();
            TypeHandler typeHandler = parameterMapping.getTypeHandler();
            //对参数赋值
            typeHandler.setParameter(versionStmt, 1, id,parameterMapping.getJdbcType());
            rs = versionStmt.executeQuery();
            Long currentVersion = 0L;
            if (rs.next()) {
                currentVersion = rs.getLong(1);
            }
            return currentVersion;
        } catch (SQLException e) {
            logger.error("查询当前version出错", e);
            throw e;
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException e) {
                    logger.error("exception happens when doing: ResultSet.close()", e);
                }
            }
            if (versionStmt != null) {
                try {
                    versionStmt.close();
                } catch (SQLException e) {
                    logger.error("exception happens when doing: PreparedStatement.close()", e);
                }
            }
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    logger.error("exception happens when doing: Connection.close()", e);
                }
            }
        }
    }
}