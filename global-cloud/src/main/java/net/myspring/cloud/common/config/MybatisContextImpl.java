package net.myspring.cloud.common.config;

import net.myspring.cloud.common.dataSource.DynamicDataSourceContext;
import net.myspring.cloud.common.enums.DataSourceTypeEnum;
import net.myspring.cloud.common.utils.SecurityUtils;
import net.myspring.mybatis.context.MybatisContext;
import net.myspring.mybatis.dialect.Dialect;
import net.myspring.mybatis.dialect.MySQLDialect;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by liuj on 2017/3/21.
 */
public class MybatisContextImpl implements MybatisContext {
    @Autowired
    private SecurityUtils securityUtils;
    @Override
    public String getAccountId() {
        return securityUtils.getAccountId();
    }

    @Override
    public Dialect getDialect() {
        if(DataSourceTypeEnum.LOCAL.name().equals(DynamicDataSourceContext.get().getDataSourceType())) {
            return new MySQLDialect();
        } else {
            return new MySQLDialect();
        }
    }
}
