package com.webflux.webflux.security.router;

import com.webflux.webflux.security.handler.AuthHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

@Configuration
@Slf4j
public class RouterAuth {
    private static final String PATH = "auth/";

    @Bean
    RouterFunction<ServerResponse> authRouter(AuthHandler authHandler) {
        return RouterFunctions.route()
                .POST(PATH + "login", authHandler::login)
                .POST(PATH + "create", authHandler::create).build();


    }
}
