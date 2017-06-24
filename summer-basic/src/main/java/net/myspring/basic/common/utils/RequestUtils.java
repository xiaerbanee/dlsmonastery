package net.myspring.basic.common.utils;

import com.google.common.collect.Maps;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.provider.OAuth2Authentication;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by liuj on 2017/4/2.
 */
public class RequestUtils {

    public static String getAccountId() {
        return getSecurityMap().get("accountId");
    }

    public static String getCompanyId() {
        return getSecurityMap().get("companyId");
    }


    public static String getCompanyName() {
        return getSecurityMap().get("companyName");
    }

    public static String getPositionId() {
        return getSecurityMap().get("positionId");
    }

    public static String getDataSourceType() {
        return getSecurityMap().get("dataSourceType");
    }

    public static String getOfficeId() {
        return getSecurityMap().get("officeId");
    }

    public static String getEmployeeId() {
        return getSecurityMap().get("employeeId");
    }


    public  static Map<String, String> getSecurityMap() {
        OAuth2Authentication auth = (OAuth2Authentication)SecurityContextHolder.getContext().getAuthentication();
        LinkedHashMap principal = Maps.newLinkedHashMap();
        if(auth !=  null) {
            principal= (LinkedHashMap) ((LinkedHashMap)auth.getUserAuthentication().getDetails()).get("principal");
        }
        return principal;
    }
}