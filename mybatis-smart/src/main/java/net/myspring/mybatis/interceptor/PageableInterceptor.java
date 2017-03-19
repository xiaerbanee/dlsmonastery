package net.myspring.mybatis.interceptor;

import net.myspring.mybatis.context.MybatisContext;
import org.apache.ibatis.binding.MapperMethod.ParamMap;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.executor.parameter.ParameterHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.MappedStatement.Builder;
import org.apache.ibatis.mapping.ParameterMapping;
import org.apache.ibatis.mapping.SqlSource;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.scripting.defaults.DefaultParameterHandler;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 * 分页插件，使用 Pageable 和 Page 对象作为输入和输出。
 * 只要方法参数中包含 Pageable 参数，则自动分页。
 * <p>
 *
 * @author liuj
 */
@Intercepts({@Signature(type = Executor.class, method = "query", args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class})})
public class PageableInterceptor implements Interceptor {
    @Autowired
    private MybatisContext mybatisContext;

    private static Logger logger = LoggerFactory.getLogger(PageableInterceptor.class);

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        final Object[] queryArgs = invocation.getArgs();
        // 查找方法参数中的 分页请求对象
        Pageable pageRequest = findPageableObject(queryArgs[1]);
        // 如果需要分页
        if (pageRequest != null) {
            final MappedStatement mappedStatement = (MappedStatement) queryArgs[0];
            final Object parameter = queryArgs[1];
            final BoundSql boundSql = mappedStatement.getBoundSql(parameter);
            // 删除尾部的 ';'
            String sql = boundSql.getSql().trim().replaceAll(";$", "");
            // 1. 搞定总记录数（如果需要的话）
            int total = queryTotal(sql, mappedStatement, boundSql);
            // 2. 搞定limit 查询
            // 2.1 获取分页SQL，并完成参数准备

            String limitSql = mybatisContext.getDialect().getPageableSql(sql, pageRequest);
            queryArgs[2] = new RowBounds(RowBounds.NO_ROW_OFFSET, RowBounds.NO_ROW_LIMIT);
            queryArgs[0] = copyFromNewSql(mappedStatement, boundSql, limitSql);
            // 2.2 继续执行剩余步骤，获取查询结果
            Object ret = invocation.proceed();
            // 3. 组成分页对象
            Page<Object> page = new PageImpl((List<Object>) ret, pageRequest, total);
            // 4. MyBatis 需要返回一个List对象，这里只是满足MyBatis而作的临时包装
            List<Page<?>> list = new ArrayList<Page<?>>(1);
            list.add(page);
            return list;
        }
        return invocation.proceed();
    }

    /**
     * 在方法参数中查找 分页请求对象
     *
     * @param params Mapper接口方法中的参数对象
     * @return
     */
    private Pageable findPageableObject(Object params) {
        if (params == null) {
            return null;
        }
        // 单个参数 表现为参数对象
        if (Pageable.class.isAssignableFrom(params.getClass())) {
            return (Pageable) params;
        } else if (params instanceof ParamMap) {
            // 多个参数 表现为 ParamMap
            ParamMap<Object> paramMap = (ParamMap<Object>) params;
            for (Map.Entry<String, Object> entry : paramMap.entrySet()) {
                Object paramValue = entry.getValue();
                if (paramValue != null && Pageable.class.isAssignableFrom(paramValue.getClass())) {
                    return (Pageable) paramValue;
                }
            }
        }
        return null;
    }

    @Override
    public Object plugin(Object target) {
        //return Plugin.wrap(target, this);
        if (Executor.class.isAssignableFrom(target.getClass())) {
            return Plugin.wrap(target, this);
        }
        return target;
    }

    @Override
    public void setProperties(Properties p) {
    }

    /**
     * 查询总记录数
     *
     * @param sql
     * @param mappedStatement
     * @param boundSql
     * @return
     * @throws SQLException
     */
    private int queryTotal(String sql, MappedStatement mappedStatement,BoundSql boundSql) throws SQLException {
        Connection connection = null;
        PreparedStatement countStmt = null;
        ResultSet rs = null;
        try {
            connection = mappedStatement.getConfiguration().getEnvironment().getDataSource().getConnection();
            String countSql = mybatisContext.getDialect().getCountSql(sql);
            countStmt = connection.prepareStatement(countSql);
            BoundSql countBoundSql = new BoundSql(mappedStatement.getConfiguration(), countSql,boundSql.getParameterMappings(), boundSql.getParameterObject());
            //对参数赋值
            ParameterHandler parameterHandler = new DefaultParameterHandler(mappedStatement, boundSql.getParameterObject(), countBoundSql);
            parameterHandler.setParameters(countStmt);

            rs = countStmt.executeQuery();
            int totalCount = 0;
            if (rs.next()) {
                totalCount = rs.getInt(1);
            }
            return totalCount;
        } catch (SQLException e) {
            logger.error("查询总记录数出错", e);
            throw e;
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException e) {
                    logger.error("exception happens when doing: ResultSet.close()", e);
                }
            }
            if (countStmt != null) {
                try {
                    countStmt.close();
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

    private MappedStatement copyFromNewSql(MappedStatement ms,BoundSql boundSql, String sql) {
        BoundSql newBoundSql = copyFromBoundSql(ms, boundSql, sql);
        return copyFromMappedStatement(ms, new BoundSqlSqlSource(newBoundSql));
    }

    private  class BoundSqlSqlSource implements SqlSource {
        private BoundSql boundSql;

        public BoundSqlSqlSource(BoundSql boundSql) {
            this.boundSql = boundSql;
        }

        public BoundSql getBoundSql(Object parameterObject) {
            return boundSql;
        }
    }

    private BoundSql copyFromBoundSql(MappedStatement ms, BoundSql boundSql, String sql) {
        BoundSql newBoundSql = new BoundSql(ms.getConfiguration(), sql, boundSql.getParameterMappings(), boundSql.getParameterObject());
        for (ParameterMapping mapping : boundSql.getParameterMappings()) {
            String prop = mapping.getProperty();
            if (boundSql.hasAdditionalParameter(prop)) {
                newBoundSql.setAdditionalParameter(prop, boundSql.getAdditionalParameter(prop));
            }
        }
        return newBoundSql;
    }

    //see: MapperBuilderAssistant
    private MappedStatement copyFromMappedStatement(MappedStatement ms, SqlSource newSqlSource) {
        Builder builder = new Builder(ms.getConfiguration(), ms.getId(), newSqlSource, ms.getSqlCommandType());
        builder.resource(ms.getResource());
        builder.fetchSize(ms.getFetchSize());
        builder.statementType(ms.getStatementType());
        builder.keyGenerator(ms.getKeyGenerator());
        if (ms.getKeyProperties() != null && ms.getKeyProperties().length != 0) {
            StringBuffer keyProperties = new StringBuffer();
            for (String keyProperty : ms.getKeyProperties()) {
                keyProperties.append(keyProperty).append(",");
            }
            keyProperties.delete(keyProperties.length() - 1, keyProperties.length());
            builder.keyProperty(keyProperties.toString());
        }
        //setStatementTimeout()
        builder.timeout(ms.getTimeout());
        //setStatementResultMap()
        builder.parameterMap(ms.getParameterMap());
        //setStatementResultMap()
        builder.resultMaps(ms.getResultMaps());
        builder.resultSetType(ms.getResultSetType());
        //setStatementCache()
        builder.cache(ms.getCache());
        builder.flushCacheRequired(ms.isFlushCacheRequired());
        builder.useCache(ms.isUseCache());
        return builder.build();
    }
}
