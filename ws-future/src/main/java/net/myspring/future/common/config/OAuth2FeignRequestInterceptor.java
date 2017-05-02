package net.myspring.future.common.config;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import net.myspring.future.common.utils.SecurityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationDetails;

public class OAuth2FeignRequestInterceptor implements RequestInterceptor {

    /**
     * The logger instance used by this class.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(OAuth2FeignRequestInterceptor.class);

    /**
     * The authorization header name.
     */
    private static final String AUTHORIZATION_HEADER = "Authorization";

    /**
     * The {@code Bearer} token type.
     */
    private static final String BEARER_TOKEN_TYPE = "Bearer";

    /**
     * {@inheritDoc}
     */
    @Override
    public void apply(RequestTemplate template) {
        System.out.println(SecurityUtils.getAccountId());

        if (template.headers().containsKey(AUTHORIZATION_HEADER)) {
            LOGGER.warn("The Authorization token has been already set");
        } else {
            LOGGER.debug("Constructing Header {} for Token {}", AUTHORIZATION_HEADER, BEARER_TOKEN_TYPE);
            template.header(AUTHORIZATION_HEADER, String.format("%s %s", BEARER_TOKEN_TYPE,
                    ((OAuth2AuthenticationDetails)SecurityContextHolder.getContext().getAuthentication().getDetails()).getTokenValue()));
        }
    }
}