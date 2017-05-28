package net.myspring.basic.common.filter;

import net.myspring.basic.common.utils.RequestUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.hibernate.Filter;
import org.hibernate.Session;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.lang.reflect.Method;

@Aspect
@Component
public class GlobalFilter {

    @PersistenceContext
    private EntityManager entityManager;

    @Pointcut("this(org.springframework.data.repository.Repository)")
    protected void repositoryExecution(){

    }

    @Before("repositoryExecution()")
    public void enableFilter(){
        Filter filter = entityManager.unwrap(Session.class).enableFilter("companyFilter");
        filter.setParameter("companyId", RequestUtils.getCompanyId());
    }

    @After("repositoryExecution()")
    public void disableFilter(){
        entityManager.unwrap(Session.class).disableFilter("companyFilter");
    }
}