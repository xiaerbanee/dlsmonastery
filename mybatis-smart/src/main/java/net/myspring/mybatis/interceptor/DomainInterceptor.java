package net.myspring.mybatis.interceptor;

import net.myspring.mybatis.context.ProviderContextUtils;
import net.myspring.mybatis.dto.TableDto;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.executor.parameter.ParameterHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.scripting.defaults.DefaultParameterHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CachePut;

import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * Created by liuj on 2017/4/26.
 */
@Intercepts({@Signature(type = Executor.class, method = "update", args = {MappedStatement.class, Object.class})})
public class DomainInterceptor implements Interceptor {

    private final Logger logger = LoggerFactory.getLogger(DomainInterceptor.class);

    @Override
    public Object intercept(Invocation invocation) throws NoSuchFieldException, InvocationTargetException, IllegalAccessException, SQLException {
        MappedStatement mappedStatement = (MappedStatement) invocation.getArgs()[0];
        SqlCommandType sqlCommandType = mappedStatement.getSqlCommandType();
        Object parameter = invocation.getArgs()[1];
        final BoundSql boundSql = mappedStatement.getBoundSql(parameter);
        if (parameter != null && invocation.getMethod().getAnnotation(CachePut.class) !=null) {
            if (ProviderContextUtils.getEntityClass(parameter.getClass()) != null) {
                TableDto tableDto = ProviderContextUtils.getTableDto(parameter.getClass());
                Object result = parameter;
                if (SqlCommandType.UPDATE == sqlCommandType) {
                    String sql = "select * from " + tableDto.getJdbcTable() + " where " + tableDto.getIdColumn().getJdbcColumn() + " = #{" + tableDto.getIdColumn().getJavaInstance()  + "}";
                    invocation.proceed();
                    result = findOne(sql,mappedStatement,boundSql);
                } else if (SqlCommandType.INSERT == sqlCommandType){
                    invocation.proceed();
                }
                // 4. MyBatis 需要返回一个List对象，这里只是满足MyBatis而作的临时包装
                List<Object> list = new ArrayList<>(1);
                list.add(result);
                return list;
            }
        }
        return invocation.proceed();
    }

    @Override
    public Object plugin(Object target) {
        if (target instanceof Executor) {
            return Plugin.wrap(target, this);
        } else {
            return target;
        }
    }

    @Override
    public void setProperties(Properties properties) {
    }

    /**
     * 查询当前对象
     *
     * @param sql
     * @param mappedStatement
     * @param boundSql
     * @return
     * @throws SQLException
     */
    private Object findOne(String sql,MappedStatement mappedStatement,BoundSql boundSql) throws SQLException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet rs = null;
        try {
            connection = mappedStatement.getConfiguration().getEnvironment().getDataSource().getConnection();
            preparedStatement = connection.prepareStatement(sql);
            BoundSql countBoundSql = new BoundSql(mappedStatement.getConfiguration(), sql,boundSql.getParameterMappings(), boundSql.getParameterObject());
            //对参数赋值
            ParameterHandler parameterHandler = new DefaultParameterHandler(mappedStatement, boundSql.getParameterObject(), countBoundSql);
            parameterHandler.setParameters(preparedStatement);

            rs = preparedStatement.executeQuery();
            Object result = null;
            if (rs.next()) {
                result = rs.getObject(1);
            }
            return result;
        } catch (SQLException e) {
            logger.error("查询录数出错", e);
            throw e;
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException e) {
                    logger.error("exception happens when doing: ResultSet.close()", e);
                }
            }
            if (preparedStatement != null) {
                try {
                    preparedStatement.close();
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
