package roomescape.auth;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AuthConfig {

    @Bean
    JwtTokenManager jwtTokenManager() {
        return new JwtTokenManager();
    }
}
