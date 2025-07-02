package com.demohouse.authentication.configuration;

import ir.kavoshgaran.authentication.configuration.properties.AuthenticationProperties;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.builders.InMemoryClientDetailsServiceBuilder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.error.OAuth2AccessDeniedHandler;
import org.springframework.security.oauth2.provider.token.AccessTokenConverter;
import org.springframework.security.oauth2.provider.token.TokenStore;

import java.util.Map;

@Configuration
@EnableAuthorizationServer
@Import(SecurityConfiguration.class)
public class AuthorizationServerConfiguration extends AuthorizationServerConfigurerAdapter {

    private final TokenStore tokenStore;
    private final UserDetailsService userDetailsService;
    private final AuthenticationManager authenticationManager;
    private final AccessTokenConverter accessTokenConverter;
    private final AuthenticationProperties authenticationProperties;
    private final PasswordEncoder passwordEncoder;

    public AuthorizationServerConfiguration(TokenStore tokenStore,
                                            @Qualifier("customUserDetailsService") UserDetailsService userDetailsService,
                                            AuthenticationManager authenticationManager,
                                            AccessTokenConverter accessTokenConverter,
                                            AuthenticationProperties authenticationProperties,
                                            PasswordEncoder passwordEncoder) {
        this.tokenStore = tokenStore;
        this.userDetailsService = userDetailsService;
        this.authenticationManager = authenticationManager;
        this.accessTokenConverter = accessTokenConverter;
        this.authenticationProperties = authenticationProperties;
        this.passwordEncoder = passwordEncoder;
    }


    @Bean
    public OAuth2AccessDeniedHandler oauthAccessDeniedHandler() {
        return new OAuth2AccessDeniedHandler();
    }

    @Override
    public void configure(AuthorizationServerSecurityConfigurer oauthServer) {
        oauthServer
                .tokenKeyAccess("permitAll()")
                .checkTokenAccess("isAuthenticated()");
    }

    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        super.configure(clients);
        InMemoryClientDetailsServiceBuilder inMemoryClientDetailsServiceBuilder = clients.inMemory();
        Map<String, AuthenticationProperties.Server.Client> clientsMap = authenticationProperties.getServer().getClients();
        for (String clientId : clientsMap.keySet()) {
            AuthenticationProperties.Server.Client loadedClient = clientsMap.get(clientId);
            inMemoryClientDetailsServiceBuilder
                    .withClient(loadedClient.getClientId())
                    .secret(passwordEncoder.encode(loadedClient.getClientSecret()))
                    .authorizedGrantTypes("password", "refresh_token")
                    .scopes("read", "write")
                    .accessTokenValiditySeconds(authenticationProperties.getServer().getAccessTokenValidity())
                    .refreshTokenValiditySeconds(authenticationProperties.getServer().getRefreshTokenValidity());
        }
    }

    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        super.configure(endpoints);
        endpoints
                .tokenStore(tokenStore)
                .userDetailsService(userDetailsService)
                .authenticationManager(authenticationManager)
                .accessTokenConverter(accessTokenConverter);
    }

}
