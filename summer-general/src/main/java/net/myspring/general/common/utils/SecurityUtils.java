package net.myspring.general.common.utils;

import net.myspring.util.json.ObjectMapperUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.jwt.Jwt;
import org.springframework.security.jwt.JwtHelper;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationDetails;

import java.util.Map;

/**
 * Created by liuj on 2017/4/2.
 */
public class SecurityUtils {

    public static String getAccountId() {
        return String.valueOf(getAdditionalInformation().get("accountId"));
    }

    public static String getCompanyId() {
        return String.valueOf(getAdditionalInformation().get("companyId"));
    }

    public static String getPositionId() {
        return String.valueOf(getAdditionalInformation().get("positionId"));
    }

    public static String getOfficeId() {
        return String.valueOf(getAdditionalInformation().get("officeId"));
    }

    public static String getEmployeeId() {
        return String.valueOf(getAdditionalInformation().get("employeeId"));
    }

    private  static Map<String, Object> getAdditionalInformation() {
        final OAuth2AuthenticationDetails details = (OAuth2AuthenticationDetails) SecurityContextHolder.getContext().getAuthentication().getDetails();
        Jwt jwt = JwtHelper.decode(details.getTokenValue());
        Map<String,Object> map = ObjectMapperUtils.readValue(jwt.getClaims(),Map.class);
        return map;
    }
}