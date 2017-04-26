package net.myspring.basic.common.utils;

import com.google.common.collect.Maps;
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
        Object details = SecurityContextHolder.getContext().getAuthentication().getDetails();
        Map<String,Object> map = Maps.newHashMap();
        if(details instanceof  OAuth2AuthenticationDetails) {
            OAuth2AuthenticationDetails oAuth2AuthenticationDetails = (OAuth2AuthenticationDetails)details;
            Jwt jwt = JwtHelper.decode(oAuth2AuthenticationDetails.getTokenValue());
            map = ObjectMapperUtils.readValue(jwt.getClaims(),Map.class);
        }
        return map;
    }
}
