package net.myspring.cloud.common.dataSource;

/**
 * Created by admin on 2016/9/7.
 */

import net.myspring.cloud.common.dataSource.annotation.KingdeeDataSource;
import net.myspring.cloud.common.dataSource.annotation.LocalDataSource;
import net.myspring.cloud.common.enums.DataSourceTypeEnum;
import net.myspring.cloud.common.utils.SecurityUtils;
import org.apache.commons.lang3.StringUtils;
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
        if(DynamicDataSourceContext.get().isAutomaticSetCompany()) {
            DynamicDataSourceContext.get().setCompanyId(securityUtils.getCompanyId());
        }
        if(DataSourceTypeEnum.LOCAL.name().equals(dataSourceType)) {
            DynamicDataSourceContext.get().setDataSourceType(DataSourceTypeEnum.LOCAL.name());
        } else {
            DynamicDataSourceContext.get().setDataSourceType(DataSourceTypeEnum.KINGDEE.name());
        }
    }

}
