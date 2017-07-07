package net.myspring.cloud.common.dataSource;

/**
 * Created by admin on 2016/9/7.
 */

import net.myspring.cloud.common.dataSource.annotation.KingdeeDataSource;
import net.myspring.cloud.common.dataSource.annotation.LocalDataSource;
import net.myspring.cloud.common.enums.DataSourceTypeEnum;
import net.myspring.cloud.common.utils.RequestUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

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
        String dataSourceType =  DataSourceTypeEnum.KINGDEE.name();
        if(method.isAnnotationPresent(LocalDataSource.class)|| method.isAnnotationPresent(KingdeeDataSource.class)) {
            if(method.isAnnotationPresent(LocalDataSource.class)) {
                dataSourceType = DataSourceTypeEnum.LOCAL.name();
            } else if (method.isAnnotationPresent(KingdeeDataSource.class)) {
                dataSourceType =  DataSourceTypeEnum.KINGDEE.name();
            }
        } else {
            if(target.getClass().isAnnotationPresent(LocalDataSource.class) || target.getClass().isAnnotationPresent(KingdeeDataSource.class)) {
                if(target.getClass().isAnnotationPresent(LocalDataSource.class)) {
                    dataSourceType = DataSourceTypeEnum.LOCAL.name();
                } else if (target.getClass().isAnnotationPresent(KingdeeDataSource.class)) {
                    dataSourceType = DataSourceTypeEnum.KINGDEE.name();
                }
            }
        }
        DbContextHolder.get().setDataSourceType(dataSourceType);
    }
}
