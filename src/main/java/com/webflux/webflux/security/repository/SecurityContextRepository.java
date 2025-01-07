package com.webflux.webflux.security.repository;

import com.webflux.webflux.security.jwt.JwtAuthenticationManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.security.web.server.context.ServerSecurityContextRepository;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
@Slf4j
@RequiredArgsConstructor
public class SecurityContextRepository implements ServerSecurityContextRepository {
    private final JwtAuthenticationManager jwtAuthenticationManager;

    @Override
    public Mono<Void> save(ServerWebExchange exchange, SecurityContext context) {
        return Mono.empty();
    }

    @Override
    public Mono<SecurityContext> load(ServerWebExchange exchange) {

        String token = exchange.getAttribute("token") ;
        if(token != null){
            return getJwtAuthenticationManager(token);
        }else{
            String authHeader = exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                return Mono.empty();
            }

            log.debug("Token in SecurityContextRepository: {}", authHeader.replace("Bearer ", ""));
            return getJwtAuthenticationManager(authHeader.replace("Bearer ", ""));
        }

    }

    public Mono<SecurityContext> getJwtAuthenticationManager(String token){
        return jwtAuthenticationManager.authenticate(new UsernamePasswordAuthenticationToken(token,token))
                .map(SecurityContextImpl::new);
    }
}
