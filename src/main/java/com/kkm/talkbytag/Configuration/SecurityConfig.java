package com.kkm.talkbytag.Configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;

@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {
    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {
        return http
                .csrf().disable() // Disable CSRF protection for simplicity, enable and configure it according to your needs
                .authorizeExchange()
                .pathMatchers("/submit").authenticated() // Require authentication for /submit path
                .anyExchange().permitAll() // Allow other requests without authentication
                .and()
                .httpBasic() // Enable HTTP Basic authentication
                .and()
                .build();
    }
}
