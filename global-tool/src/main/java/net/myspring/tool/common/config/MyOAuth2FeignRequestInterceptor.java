package net.myspring.tool.common.config;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.OAuth2ClientContext;
import org.springframework.security.oauth2.client.http.AccessTokenRequiredException;
import org.springframework.security.oauth2.client.resource.OAuth2ProtectedResourceDetails;
import org.springframework.security.oauth2.client.resource.UserRedirectRequiredException;
import org.springframework.security.oauth2.client.token.AccessTokenProvider;
import org.springframework.security.oauth2.client.token.AccessTokenProviderChain;
import org.springframework.security.oauth2.client.token.AccessTokenRequest;
import org.springframework.security.oauth2.client.token.grant.client.ClientCredentialsAccessTokenProvider;
import org.springframework.security.oauth2.client.token.grant.code.AuthorizationCodeAccessTokenProvider;
import org.springframework.security.oauth2.client.token.grant.implicit.ImplicitAccessTokenProvider;
import org.springframework.security.oauth2.client.token.grant.password.ResourceOwnerPasswordAccessTokenProvider;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationDetails;

import java.util.Arrays;

public class MyOAuth2FeignRequestInterceptor implements RequestInterceptor {

    private final OAuth2ProtectedResourceDetails resource;

    private final String tokenType =  "Bearer";

    private final String header = "Authorization";

    /**
     * Fully customizable constructor for changing token type and header name, in cases of
     * Bearer and Authorization is not the default such as "bearer", "authorization"
     *
     * @param resource type of resource to be accessed
     */
    public MyOAuth2FeignRequestInterceptor(OAuth2ProtectedResourceDetails resource) {
        this.resource = resource;
    }

    private AccessTokenProvider accessTokenProvider = new AccessTokenProviderChain(Arrays
            .<AccessTokenProvider> asList(new AuthorizationCodeAccessTokenProvider(),
                    new ImplicitAccessTokenProvider(),
                    new ResourceOwnerPasswordAccessTokenProvider(),
                    new ClientCredentialsAccessTokenProvider()));


    /**
     * Create a template with the header of provided name and extracted extract
     *
     * @see RequestInterceptor#apply(RequestTemplate)
     */
    @Override
    public void apply(RequestTemplate template) {
        template.header(header, extract(tokenType));
    }

    /**
     * Extracts the token extract id the access token exists or returning an empty extract
     * if there is no one on the context it may occasionally causes Unauthorized response
     * since the token extract is empty
     *
     * @param tokenType type name of token
     * @return token value from context if it exists otherwise empty String
     */
    protected String extract(String tokenType) {
        OAuth2AccessToken accessToken = getToken();
        return String.format("%s %s", tokenType, accessToken.getValue());
    }

    /**
     * Extract the access token within the request or try to acquire a new one by
     * delegating it to {@link #acquireAccessToken()}
     *
     * @return valid token
     */
    public OAuth2AccessToken getToken() {
        OAuth2AccessToken accessToken = null;
        Authentication principal = SecurityContextHolder.getContext().getAuthentication();
        if (principal instanceof OAuth2Authentication) {
            OAuth2Authentication authentication = (OAuth2Authentication) principal;
            Object details = authentication.getDetails();
            if (details instanceof OAuth2AuthenticationDetails) {
                OAuth2AuthenticationDetails oauthsDetails = (OAuth2AuthenticationDetails) details;
                String token = oauthsDetails.getTokenValue();
                accessToken = new DefaultOAuth2AccessToken(token);
            }
        }
        if (accessToken == null || accessToken.isExpired()) {
            accessToken = acquireAccessToken();
        }
        return accessToken;
    }

    /**
     * Try to acquire the token using a access token provider
     *
     * @throws UserRedirectRequiredException in case the user needs to be redirected to an
     * approval page or login page
     * @return valid access token
     */
    protected OAuth2AccessToken acquireAccessToken()
            throws UserRedirectRequiredException {
        OAuth2ClientContext oAuth2ClientContext = null;
        AccessTokenRequest tokenRequest = oAuth2ClientContext.getAccessTokenRequest();
        if (tokenRequest == null) {
            throw new AccessTokenRequiredException(
                    "Cannot find valid context on request for resource '"
                            + resource.getId() + "'.",
                    resource);
        }
        String stateKey = tokenRequest.getStateKey();
        if (stateKey != null) {
            tokenRequest.setPreservedState(
                    oAuth2ClientContext.removePreservedState(stateKey));
        }
        OAuth2AccessToken existingToken = oAuth2ClientContext.getAccessToken();
        if (existingToken != null) {
            oAuth2ClientContext.setAccessToken(existingToken);
        }
        OAuth2AccessToken obtainableAccessToken;
        obtainableAccessToken = accessTokenProvider.obtainAccessToken(resource,
                tokenRequest);
        if (obtainableAccessToken == null || obtainableAccessToken.getValue() == null) {
            throw new IllegalStateException(
                    " Access token provider returned a null token, which is illegal according to the contract.");
        }
        oAuth2ClientContext.setAccessToken(obtainableAccessToken);
        return obtainableAccessToken;
    }

    public void setAccessTokenProvider(AccessTokenProvider accessTokenProvider) {
        this.accessTokenProvider = accessTokenProvider;
    }

}
