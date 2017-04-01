package net.myspring.uaa.security;

import com.google.common.collect.Maps;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by liuj on 2017/4/1.
 */
public class CustomJwtAccessTokenConverter extends JwtAccessTokenConverter  {
    @Override
    public OAuth2AccessToken enhance(OAuth2AccessToken accessToken,OAuth2Authentication authentication) {
        CustomUserDetails  customUserDetails= (CustomUserDetails) authentication.getUserAuthentication().getPrincipal();
        Map<String, Object> info = Maps.newHashMap(accessToken.getAdditionalInformation());
        info.put("companyId",customUserDetails.getCompanyId());
        info.put("positionId", customUserDetails.getPositionId());
        DefaultOAuth2AccessToken customAccessToken = new DefaultOAuth2AccessToken(accessToken);
        customAccessToken.setAdditionalInformation(info);
        return super.enhance(customAccessToken, authentication);
    }
}
