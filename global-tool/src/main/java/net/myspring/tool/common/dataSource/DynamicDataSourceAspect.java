package net.myspring.tool.common.dataSource;

/**
 * Created by admin on 2016/9/7.
 */

import net.myspring.tool.common.dataSource.annotation.FactoryDataSource;
import net.myspring.tool.common.dataSource.annotation.SysDataSource;
import net.myspring.tool.common.enums.DataSourceTypeEnum;
import net.myspring.tool.common.utils.SecurityUtils;
import org.aspectj.lang.JoinPoint;
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
        String dataSourceType =  DataSourceTypeEnum.SYS.name();
        if(method.isAnnotationPresent(SysDataSource.class)|| method.isAnnotationPresent(FactoryDataSource.class)) {
            if(method.isAnnotationPresent(SysDataSource.class)) {
                dataSourceType = DataSourceTypeEnum.SYS.name();
            } else if (method.isAnnotationPresent(FactoryDataSource.class)) {
                dataSourceType =  DataSourceTypeEnum.FACTORY.name();
            }
        } else {
            if(target.getClass().isAnnotationPresent(SysDataSource.class) || target.getClass().isAnnotationPresent(FactoryDataSource.class)) {
                if(target.getClass().isAnnotationPresent(SysDataSource.class)) {
                    dataSourceType = DataSourceTypeEnum.SYS.name();
                } else if (target.getClass().isAnnotationPresent(FactoryDataSource.class)) {
                    dataSourceType = DataSourceTypeEnum.FACTORY.name();
                }
            }
        }
        DynamicDataSourceContext.get().setCompanyId(securityUtils.getCompanyId());
        if(DataSourceTypeEnum.SYS.name().equals(dataSourceType)) {
            DynamicDataSourceContext.get().setDataSourceType(DataSourceTypeEnum.SYS.name());
        } else {
            DynamicDataSourceContext.get().setDataSourceType(DataSourceTypeEnum.FACTORY.name());
        }
    }

}
