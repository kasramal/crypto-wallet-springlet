package ir.kavoshgaran.authentication.configuration.properties;


import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;
import java.util.Map;

@Getter
@Setter
@ConfigurationProperties(prefix = "authentication")
public class AuthenticationProperties {

    private Server server;
    private JWT jwt;
    private List<User> users;

    @Getter
    @Setter
    public static class User {
        private String username;
        private String password;
    }

    @Getter
    @Setter
    public static class JWT {
        private String signingKey;
    }

    @Getter
    @Setter
    public static class Server {
        private int accessTokenValidity;
        private int refreshTokenValidity;
        private Map<String, Client> clients;

        @Getter
        @Setter
        public static class Client {
            private String clientId;
            private String clientSecret;
        }
    }
}
