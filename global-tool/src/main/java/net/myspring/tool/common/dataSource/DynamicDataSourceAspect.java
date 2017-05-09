package net.myspring.tool.common.dataSource;

/**
 * Created by admin on 2016/9/7.
 */

import net.myspring.tool.common.dataSource.annotation.FactoryDataSource;
import net.myspring.tool.common.dataSource.annotation.LocalDataSource;
import net.myspring.tool.common.enums.DataSourceTypeEnum;
import net.myspring.tool.common.utils.RequestUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;

@Aspect
@Component
@Order(1)
public class DynamicDataSourceAspect {

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
        HttpServletRequest request  = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        request.setAttribute("dataSourceType",dataSourceType);
    }
}
