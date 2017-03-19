package net.myspring.hr.config;

import net.myspring.mybatis.context.MybatisContext;
import net.myspring.mybatis.dialect.Dialect;
import net.myspring.mybatis.dialect.MySQLDialect;

/**
 * Created by liuj on 2017/3/19.
 */
public class MybatisContextImpl implements MybatisContext {
    @Override
    public String getAccountId() {
        return "1";
    }

    @Override
    public Dialect getDialect() {
        return new MySQLDialect();
    }
}
