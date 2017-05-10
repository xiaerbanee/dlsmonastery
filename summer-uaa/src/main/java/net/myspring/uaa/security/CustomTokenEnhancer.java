package net.myspring.uaa.security;

import com.google.common.collect.Maps;
import net.myspring.util.time.LocalDateTimeUtils;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * Created by liuj on 2017/4/2.
 */
public class CustomTokenEnhancer implements TokenEnhancer {
    @Override
    public OAuth2AccessToken enhance(OAuth2AccessToken accessToken, OAuth2Authentication authentication) {
        CustomUserDetails  customUserDetails= (CustomUserDetails) authentication.getUserAuthentication().getPrincipal();
        Map<String, Object> additionalInfo = Maps.newHashMap();
        additionalInfo.put("companyId",customUserDetails.getCompanyId());
        additionalInfo.put("positionId", customUserDetails.getPositionId());
        additionalInfo.put("accountId",customUserDetails.getAccountId());
        additionalInfo.put("officeId",customUserDetails.getOfficeId());
        additionalInfo.put("employeeId",customUserDetails.getEmployeeId());
        ((DefaultOAuth2AccessToken) accessToken).setAdditionalInformation(additionalInfo);
        return accessToken;
    }
}
