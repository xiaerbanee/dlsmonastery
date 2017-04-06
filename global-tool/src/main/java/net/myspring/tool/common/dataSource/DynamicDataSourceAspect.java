package net.myspring.tool.common.dataSource;

/**
 * Created by admin on 2016/9/7.
 */

import net.myspring.tool.common.dataSource.annotation.FactoryDataSource;
import net.myspring.tool.common.dataSource.annotation.LocalDataSource;
import net.myspring.tool.common.enums.DataSourceTypeEnum;
import net.myspring.tool.common.utils.SecurityUtils;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

@Aspect
@Component
@Order(1)
public class DynamicDataSourceAspect {
    @Autowired
    private SecurityUtils securityUtils;

    @Pointcut("execution(* net.myspring..*.service.*.*(..))")
    public void serviceExecution() {
    }

    @Before("serviceExecution()")
    public void setDynamicDataSource(JoinPoint point) {
        Object target = point.getTarget();
        Method method = ((MethodSignature) point.getSignature()).getMethod();
        String dataSourceType =  DataSourceTypeEnum.LOCAL.name();
        if(method.isAnnotationPresent(LocalDataSource.class)|| method.isAnnotationPresent(FactoryDataSource.class)) {
            if(method.isAnnotationPresent(LocalDataSource.class)) {
                dataSourceType = DataSourceTypeEnum.LOCAL.name();
            } else if (method.isAnnotationPresent(FactoryDataSource.class)) {
                dataSourceType =  DataSourceTypeEnum.FACTORY.name();
            }
        } else {
            if(target.getClass().isAnnotationPresent(LocalDataSource.class) || target.getClass().isAnnotationPresent(FactoryDataSource.class)) {
                if(target.getClass().isAnnotationPresent(LocalDataSource.class)) {
                    dataSourceType = DataSourceTypeEnum.LOCAL.name();
                } else if (target.getClass().isAnnotationPresent(FactoryDataSource.class)) {
                    dataSourceType = DataSourceTypeEnum.FACTORY.name();
                }
            }
        }
        if(DynamicDataSourceContext.get().isAutomaticSetCompany()) {
            DynamicDataSourceContext.get().setCompanyId(securityUtils.getCompanyId());
        }
        if(DataSourceTypeEnum.LOCAL.name().equals(dataSourceType)) {
            DynamicDataSourceContext.get().setDataSourceType(DataSourceTypeEnum.LOCAL.name());
        } else {
            DynamicDataSourceContext.get().setDataSourceType(DataSourceTypeEnum.FACTORY.name());
        }
    }

    @After("serviceExecution()")
    public void removeDynamicDataSource(JoinPoint point) {
        DynamicDataSourceContext.get().remove();
    }
}
