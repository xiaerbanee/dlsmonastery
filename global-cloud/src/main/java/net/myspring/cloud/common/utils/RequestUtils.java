package net.myspring.cloud.common.utils;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.provider.OAuth2Authentication;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by liuj on 2017/4/2.
 */
public class RequestUtils {
    public static RequestEntity getRequestEntity() {
        RequestEntity  requestEntity = new RequestEntity();
        Map<String,String> securityMap = getSecurityMap();
        if(securityMap.size()>0) {
            requestEntity.setAccountId(securityMap.get("accountId"));
            requestEntity.setCompanyId(securityMap.get("companyId"));
            requestEntity.setPositionId(securityMap.get("positionId"));
            requestEntity.setOfficeId(securityMap.get("officeId"));
            requestEntity.setEmployeeId(securityMap.get("employeeId"));
        }
        return requestEntity;
    }

    public static String getAccountId() {
        return getRequestEntity().getAccountId();
    }

    public static String getCompanyId() {
        return getRequestEntity().getCompanyId();
    }


    private  static Map<String, String> getSecurityMap() {
        OAuth2Authentication auth = (OAuth2Authentication)SecurityContextHolder.getContext().getAuthentication();
        LinkedHashMap principal= (LinkedHashMap) ((LinkedHashMap)auth.getUserAuthentication().getDetails()).get("principal");
        return principal;
    }
}