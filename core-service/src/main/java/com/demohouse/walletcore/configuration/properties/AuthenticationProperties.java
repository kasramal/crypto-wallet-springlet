package com.demohouse.walletcore.configuration.properties;


import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties(prefix = "authentication")
public class AuthenticationProperties {

    private Resource resource;
    private JWT jwt;

    @Getter
    @Setter
    public static class JWT {
        private String signingKey;
    }

    @Getter
    @Setter
    public static class Resource {
        private String clientId;
        private String clientSecret;
    }
}
