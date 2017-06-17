package net.myspring.basic.common.utils;

import com.google.common.collect.Maps;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import net.myspring.util.json.ObjectMapperUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.jwt.Jwt;
import org.springframework.security.jwt.JwtHelper;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationDetails;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * Created by liuj on 2017/4/2.
 */
public class RequestUtils {
    public static final String REQEUST_ENTITY = "requestEntity";

    public static RequestEntity getRequestEntity() {
        HttpServletRequest request  = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        RequestEntity requestEntity;
        if(request.getAttribute(REQEUST_ENTITY) != null) {
            requestEntity = (RequestEntity) request.getAttribute(REQEUST_ENTITY);
        } else {
            requestEntity = new RequestEntity();
        }
        Map<String,String> securityMap = getSecurityMap();
        if(securityMap.size()>0) {
            requestEntity.setAccountId(securityMap.get("accountId"));
            requestEntity.setCompanyId(securityMap.get("companyId"));
            requestEntity.setPositionId(securityMap.get("positionId"));
            requestEntity.setOfficeId(securityMap.get("officeId"));
            requestEntity.setEmployeeId(securityMap.get("employeeId"));
            requestEntity.setCompanyName(securityMap.get("companyName"));
        }
        request.setAttribute(REQEUST_ENTITY,requestEntity);
        return requestEntity;
    }

    public static String getAccountId() {
        return getRequestEntity().getAccountId();
    }

    public static String getCompanyId() {
        return getRequestEntity().getCompanyId();
    }

    public static DBObject getDbObject(){
        DBObject dbObject = new BasicDBObject();
        dbObject.put("createdBy", getAccountId());
        dbObject.put("companyId",getRequestEntity().getCompanyId());
        dbObject.put("positionId",getRequestEntity().getPositionId());
        dbObject.put("officeId",getRequestEntity().getOfficeId());
        return dbObject;
    }

    private  static Map<String, String> getSecurityMap() {
        Object details = SecurityContextHolder.getContext().getAuthentication().getDetails();
        Map<String,String> map = Maps.newHashMap();
        if(details instanceof  OAuth2AuthenticationDetails) {
            OAuth2AuthenticationDetails oAuth2AuthenticationDetails = (OAuth2AuthenticationDetails)details;
            Jwt jwt = JwtHelper.decode(oAuth2AuthenticationDetails.getTokenValue());
            map = ObjectMapperUtils.readValue(jwt.getClaims(),Map.class);
        }
        return map;
    }
}