package net.myspring.basic.common.config;

import net.myspring.basic.SummerBasicApplication;
import net.myspring.basic.common.utils.SecurityUtils;
import net.myspring.mybatis.context.MybatisContext;
import net.myspring.mybatis.dialect.Dialect;
import net.myspring.mybatis.dialect.MySQLDialect;
import net.myspring.util.json.ObjectMapperUtils;
import net.myspring.util.text.StringUtils;
import org.apache.commons.lang.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.jwt.Jwt;
import org.springframework.security.jwt.JwtHelper;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationDetails;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * Created by liuj on 2017/3/21.
 */
public class MybatisContextImpl implements MybatisContext {

    @Override
    public String getAccountId() {
        final OAuth2AuthenticationDetails details = (OAuth2AuthenticationDetails) SecurityContextHolder.getContext().getAuthentication().getDetails();
        Jwt jwt = JwtHelper.decode(details.getTokenValue());
        Map<String,Object> map = ObjectMapperUtils.readValue(jwt.getClaims(),Map.class);
        return ObjectUtils.toString(map.get("accountId"));
    }

    @Override
    public Dialect getDialect() {
        return new MySQLDialect();
    }
}
