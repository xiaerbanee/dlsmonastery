package net.myspring.tool.common.config;

import net.myspring.mybatis.context.MybatisContext;
import net.myspring.mybatis.dialect.Dialect;
import net.myspring.mybatis.dialect.MySQLDialect;
import net.myspring.tool.common.dataSource.DynamicDataSourceContext;
import net.myspring.tool.common.enums.DataSourceTypeEnum;
import net.myspring.tool.common.utils.SecurityUtils;
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
