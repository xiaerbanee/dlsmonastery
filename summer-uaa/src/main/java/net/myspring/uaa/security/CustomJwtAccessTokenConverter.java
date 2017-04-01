package net.myspring.uaa.security;

import net.myspring.uaa.security.CustomUserDetails;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;

/**
 * Created by liuj on 2017/4/1.
 */
public class CustomJwtAccessTokenConverter extends JwtAccessTokenConverter  {
    @Override
    public OAuth2AccessToken enhance(OAuth2AccessToken accessToken,OAuth2Authentication authentication) {
        OAuth2AccessToken enhancedToken = super.enhance(accessToken,authentication);
        CustomUserDetails  customUserDetails= (CustomUserDetails) authentication.getUserAuthentication().getPrincipal();
        enhancedToken.getAdditionalInformation().put("companyId",customUserDetails.getCompanyId());
        enhancedToken.getAdditionalInformation().put("positionId", customUserDetails.getPositionId());
        return enhancedToken;
    }
}
