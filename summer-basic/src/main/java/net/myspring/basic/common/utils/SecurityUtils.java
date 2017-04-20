package net.myspring.basic.common.utils;

import net.myspring.util.json.ObjectMapperUtils;
import net.myspring.util.text.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.jwt.Jwt;
import org.springframework.security.jwt.JwtHelper;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationDetails;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * Created by liuj on 2017/4/2.
 */
public class SecurityUtils {

    public static String getAccountId() {
        return StringUtils.toString(getAdditionalInformation().get("accountId"));
    }

    public static String getCompanyId() {
        return StringUtils.toString(getAdditionalInformation().get("companyId"));
    }

    public static String getPositionId() {
        return StringUtils.toString(getAdditionalInformation().get("positionId"));
    }

    public static String getOfficeId() {
        return StringUtils.toString(getAdditionalInformation().get("officeId"));
    }

    public static String getEmployeeId() {
        return StringUtils.toString(getAdditionalInformation().get("employeeId"));
    }

    private  static Map<String, Object> getAdditionalInformation() {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        final OAuth2AuthenticationDetails details = (OAuth2AuthenticationDetails) securityContext.getAuthentication().getDetails();
        Jwt jwt = JwtHelper.decode(details.getTokenValue());
        Map<String,Object> map = ObjectMapperUtils.readValue(jwt.getClaims(),Map.class);
        return map;
    }
}
