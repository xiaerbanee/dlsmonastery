package net.myspring.future.common.config;

import net.myspring.future.common.utils.SecurityUtils;
import net.myspring.mybatis.context.MybatisContext;
import net.myspring.mybatis.dialect.Dialect;
import net.myspring.mybatis.dialect.MySQLDialect;
import net.myspring.util.json.ObjectMapperUtils;
import org.apache.commons.lang.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.jwt.Jwt;
import org.springframework.security.jwt.JwtHelper;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationDetails;

import java.util.Map;

/**
 * Created by liuj on 2017/3/21.
 */
public class MybatisContextImpl implements MybatisContext {

    @Override
    public String getAccountId() {
        return SecurityUtils.getAccountId();
    }

    @Override
    public Dialect getDialect() {
        return new MySQLDialect();
    }
}
