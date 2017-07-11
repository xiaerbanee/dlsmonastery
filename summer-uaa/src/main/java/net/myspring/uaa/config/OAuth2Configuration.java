package net.myspring.uaa.config;

import net.myspring.uaa.security.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.store.JdbcTokenStore;

import javax.sql.DataSource;

/**
 * Created by on 28.01.16.
 *
 * @author David Steiman
 */
@Configuration
@EnableAuthorizationServer
public class OAuth2Configuration extends AuthorizationServerConfigurerAdapter {

    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private CustomUserDetailsService customUserDetailsService;
    @Autowired
    @Qualifier("oauthDataSource")
    private DataSource oauthDataSource;

    @Bean
    public JdbcTokenStore jdbcTokenStore() {
        return new JdbcTokenStore(oauthDataSource);
    }

    @Override
    public void configure(AuthorizationServerSecurityConfigurer oauthServer)
            throws Exception {
        oauthServer
                .tokenKeyAccess("permitAll()")
                .checkTokenAccess("isAuthenticated()");
    }

    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        endpoints
                .authenticationManager(authenticationManager)
                .userDetailsService(customUserDetailsService)
                .tokenStore(jdbcTokenStore());
    }

    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        clients.inMemory()
                .withClient("basic-service")
                .secret("basic-password")
                .authorizedGrantTypes("client_credentials", "refresh_token")
                .scopes("server","read")
                .accessTokenValiditySeconds(3600) // 1 hour
                .refreshTokenValiditySeconds(2592000) // 30 days
                .and()
                .withClient("ws-future-service")
                .secret("ws-future-password")
                .authorizedGrantTypes("client_credentials", "refresh_token")
                .scopes("server","read")
                .accessTokenValiditySeconds(3600) // 1 hour
                .refreshTokenValiditySeconds(2592000) // 30 days
                .and()
                .withClient("general-service")
                .secret("general-password")
                .authorizedGrantTypes("client_credentials", "refresh_token")
                .scopes("server","read")
                .accessTokenValiditySeconds(3600) // 1 hour
                .refreshTokenValiditySeconds(2592000) // 30 days
                .and()
                .withClient("cloud-service")
                .secret("cloud-password")
                .authorizedGrantTypes("client_credentials", "refresh_token")
                .scopes("server","read")
                .accessTokenValiditySeconds(3600) // 1 hour
                .refreshTokenValiditySeconds(2592000) // 30 days
                .and()
                .withClient("tool-service")
                .secret("tool-password")
                .authorizedGrantTypes("client_credentials", "refresh_token")
                .scopes("server","read")
                .accessTokenValiditySeconds(3600) // 1 hour
                .refreshTokenValiditySeconds(2592000) // 30 days
                .and()
                .withClient("ui")
                .secret("password")
                .authorizedGrantTypes("authorization_code", "refresh_token")
                .scopes("read")
                .autoApprove(true);
    }
}