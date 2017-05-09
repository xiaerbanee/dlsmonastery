package net.myspring.tool.common.config;

import net.myspring.mybatis.context.MybatisContext;
import net.myspring.mybatis.dialect.Dialect;
import net.myspring.mybatis.dialect.MySQLDialect;
import net.myspring.tool.common.enums.DataSourceTypeEnum;
import net.myspring.tool.common.utils.RequestUtils;

/**
 * Created by liuj on 2017/3/21.
 */
public class MybatisContextImpl implements MybatisContext {

    @Override
    public String getAccountId() {
        return RequestUtils.getAccountId();
    }

    @Override
    public Dialect getDialect() {
        if(DataSourceTypeEnum.LOCAL.name().equals(RequestUtils.getDataSourceType())) {
            return new MySQLDialect();
        } else {
            return new MySQLDialect();
        }
    }
}
