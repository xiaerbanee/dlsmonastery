package net.myspring.basic.common.utils;

import com.google.common.collect.Maps;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.provider.OAuth2Authentication;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by liuj on 2017/4/2.
 */
public class RequestUtils {

    public static String getAccountId() {
        return (String) getSecurityMap().get("accountId");
    }

    public static List<String> getRoleIdList() {
        return (List<String>) getSecurityMap().get("roleIdList");
    }

    public static String getCompanyName() {
        return (String) getSecurityMap().get("companyName");
    }

    public static Boolean getAdmin() {
        return (Boolean) getSecurityMap().get("admin");
    }

    public static List<String> getPositionIdList() {
        return (List<String>) getSecurityMap().get("positionIdList");
    }

    public static String getDataSourceType() {
        return (String) getSecurityMap().get("dataSourceType");
    }

    public static String getOfficeId() {
        return (String) getSecurityMap().get("officeId");
    }

    public static String getEmployeeId() {
        return (String) getSecurityMap().get("employeeId");
    }

    public static List<String> getOfficeIdList() {
        return (List<String>) getSecurityMap().get("officeIdList");
    }

    private  static Map<String, Object> getSecurityMap() {
        OAuth2Authentication auth = (OAuth2Authentication)SecurityContextHolder.getContext().getAuthentication();
        LinkedHashMap<String,Object> principal = Maps.newLinkedHashMap();
        if(auth !=  null) {
            Object object = ((LinkedHashMap)auth.getUserAuthentication().getDetails()).get("principal");
            if(object instanceof Map){
                principal= (LinkedHashMap)object;
            }
        }
        return principal;
    }
}