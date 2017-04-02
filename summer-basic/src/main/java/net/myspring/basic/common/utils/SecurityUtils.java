package net.myspring.basic.common.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationDetails;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * Created by liuj on 2017/4/2.
 */
public class SecurityUtils {
    @Autowired
    private static TokenStore tokenStore;

    public static String getAccountId() {
        return String.valueOf(getAdditionalInformation().get("accountId"));
    }

    public static String getCompanyId() {
        return String.valueOf(getAdditionalInformation().get("companyId"));
    }

    public static String getPositionId() {
        return String.valueOf(getAdditionalInformation().get("positionId"));
    }

    private static Map<String, Object> getAdditionalInformation() {
        final OAuth2AuthenticationDetails details = (OAuth2AuthenticationDetails) SecurityContextHolder.getContext().getAuthentication().getDetails();
        final OAuth2AccessToken accessToken = tokenStore.readAccessToken(details.getTokenValue());
        return accessToken.getAdditionalInformation();
    }
}
