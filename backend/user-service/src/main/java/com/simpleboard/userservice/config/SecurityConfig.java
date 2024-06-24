package com.simpleboard.userservice.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;

//키클록
@Configuration
public class SecurityConfig {

    @Bean
    public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http){
        http.authorizeExchange().pathMatchers("/**").permitAll()
                .and().authorizeExchange().anyExchange().authenticated()
                .and().oauth2Login()
                .and().csrf().disable();

        return http.build();
    }
}
