package net.myspring.cloud.common.dataSource;

/**
 * Created by admin on 2016/9/7.
 */

import net.myspring.cloud.common.dataSource.annotation.KingdeeDataSource;
import net.myspring.cloud.common.dataSource.annotation.SysDataSource;
import net.myspring.cloud.common.enums.DataSourceTypeEnum;
import net.myspring.cloud.common.utils.SecurityUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
        String dataSourceType =  DataSourceTypeEnum.KINGDEE.name();
        if(method.isAnnotationPresent(SysDataSource.class)|| method.isAnnotationPresent(KingdeeDataSource.class)) {
            if(method.isAnnotationPresent(SysDataSource.class)) {
                dataSourceType = DataSourceTypeEnum.SYS.name();
            } else if (method.isAnnotationPresent(KingdeeDataSource.class)) {
                dataSourceType =  DataSourceTypeEnum.KINGDEE.name();
            }
        } else {
            if(target.getClass().isAnnotationPresent(SysDataSource.class) || target.getClass().isAnnotationPresent(KingdeeDataSource.class)) {
                if(target.getClass().isAnnotationPresent(SysDataSource.class)) {
                    dataSourceType = DataSourceTypeEnum.SYS.name();
                } else if (target.getClass().isAnnotationPresent(KingdeeDataSource.class)) {
                    dataSourceType = DataSourceTypeEnum.KINGDEE.name();
                }
            }
        }
        if(DataSourceTypeEnum.SYS.name().equals(dataSourceType)) {
            DynamicDataSourceContext.get().setLookupKey(DataSourceTypeEnum.SYS.name());
        } else {
            DynamicDataSourceContext.get().setLookupKey(DataSourceTypeEnum.KINGDEE.name() + "_" + securityUtils.getCompanyId());
        }
    }

}
