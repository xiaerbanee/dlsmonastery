package net.myspring.mybatis.interceptor;

/**
 * Created by liuj on 2016/11/16.
 */

import net.myspring.mybatis.context.MybatisContext;
import net.myspring.mybatis.context.ProviderContextUtils;
import net.myspring.mybatis.context.ProviderMapperAspect;
import net.myspring.mybatis.dto.TableDto;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.plugin.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.ReflectionUtils;

import javax.persistence.Entity;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 * 审计接口，自动注入用户id以及自动获取注入更新时间和创建时间
 */
@Intercepts({@Signature(type = Executor.class, method = "update", args = {MappedStatement.class, Object.class})})
public class AuditingInterceptor implements Interceptor {
    private final static Logger logger = LoggerFactory.getLogger(ProviderMapperAspect.class);
    @Autowired
    private MybatisContext mybatisContext;
    @Override
    public Object intercept(Invocation invocation) throws NoSuchFieldException, InvocationTargetException, IllegalAccessException {
        MappedStatement mappedStatement = (MappedStatement) invocation.getArgs()[0];
        SqlCommandType sqlCommandType = mappedStatement.getSqlCommandType();
        Object parameter = invocation.getArgs()[1];
        if (parameter != null) {
            if (parameter.getClass().getAnnotation(Entity.class) != null) {
                TableDto tableDto = ProviderContextUtils.getTableDto(parameter.getClass());
                setFieldValue(tableDto, parameter, sqlCommandType);
            } else {
                if (Map.class.isAssignableFrom(parameter.getClass())) {
                    if(((Map) parameter).containsKey("list")) {
                        List<Object> list = (List<Object>) ((Map) parameter).get("list");
                        for (Object entity :list) {
                            if (entity.getClass().getAnnotation(Entity.class) != null) {
                                TableDto tableDto = ProviderContextUtils.getTableDto(entity.getClass());
                                setFieldValue(tableDto, entity, sqlCommandType);
                            }
                        }
                    }
                }
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

    private void setFieldValue(TableDto tableDto, Object entity, SqlCommandType sqlCommandType) throws NoSuchFieldException {
        if (SqlCommandType.UPDATE == sqlCommandType || SqlCommandType.INSERT == sqlCommandType) {
            LocalDateTime localDateTime = LocalDateTime.now();
            if (SqlCommandType.INSERT == sqlCommandType) {
                if (tableDto.getCreatedByColumn() != null) {
                    Field field =  entity.getClass().getField(tableDto.getCreatedByColumn().getJavaInstance());
                    ReflectionUtils.setField(field,entity,mybatisContext.getAccountId());
                }
                if (tableDto.getCreatedDateColumn() != null) {
                    Field field =  entity.getClass().getField(tableDto.getCreatedDateColumn().getJavaInstance());
                    ReflectionUtils.setField(field,entity, localDateTime);
                }
                if (tableDto.getVersionColumn() != null) {
                    Field field =  entity.getClass().getField(tableDto.getVersionColumn().getJavaInstance());
                    ReflectionUtils.setField(field,entity, 0L);
                }
            }
            if (tableDto.getLastModifiedByColumn() != null) {
                Field field =  entity.getClass().getField(tableDto.getLastModifiedByColumn().getJavaInstance());
                ReflectionUtils.setField(field,entity, mybatisContext.getAccountId());
            }
            if (tableDto.getLastModifiedDateColumn() != null) {
                Field field =  entity.getClass().getField(tableDto.getLastModifiedDateColumn().getJavaInstance());
                ReflectionUtils.setField(field,entity, localDateTime);
            }
        }
    }
}