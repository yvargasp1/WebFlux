package com.webflux.webflux.security.config;

import com.webflux.webflux.security.jwt.JwtFilter;
import com.webflux.webflux.security.repository.SecurityContextRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;

@Configuration
@EnableWebFluxSecurity
@EnableReactiveMethodSecurity
@RequiredArgsConstructor
public class MainSecurity {
    private final SecurityContextRepository securityContextRepository;
    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity serverHttpSecurity, JwtFilter jwtFilter){
        return serverHttpSecurity
                .csrf(ServerHttpSecurity.CsrfSpec::disable) // Disable CSRF
                .authorizeExchange(authorize -> authorize
                        .pathMatchers("/auth/**").permitAll() // Allow unauthenticated access to /auth/**
                        .anyExchange().authenticated() // Require authentication for all other requests
                )
                .addFilterBefore(jwtFilter, SecurityWebFiltersOrder.AUTHENTICATION) // Add your JWT filter
                .securityContextRepository(securityContextRepository)
                .httpBasic(ServerHttpSecurity.HttpBasicSpec::disable) // Disable HTTP Basic
                .formLogin(ServerHttpSecurity.FormLoginSpec::disable) // Disable Form Login
                .build();

    }

}

