package net.myspring.basic.common.config;

import net.myspring.basic.SummerBasicApplication;
import net.myspring.basic.common.utils.SecurityUtils;
import net.myspring.mybatis.context.MybatisContext;
import net.myspring.mybatis.dialect.Dialect;
import net.myspring.mybatis.dialect.MySQLDialect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

/**
 * Created by liuj on 2017/3/21.
 */
public class MybatisContextImpl implements MybatisContext {

    @Override
    public String getAccountId() {
        SecurityUtils securityUtils = SummerBasicApplication.getApplicationContext().getBean(SecurityUtils.class);
        return securityUtils.getAccountId();
    }

    @Override
    public Dialect getDialect() {
        return new MySQLDialect();
    }
}
