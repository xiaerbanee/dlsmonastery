package net.myspring.mybatis.context;

import net.myspring.mybatis.dialect.Dialect;

/**
 * Created by liuj on 2017/3/19.
 */
public interface MybatisContext {

    String getAccountId();

    Dialect getDialect();
}
