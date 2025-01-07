package com.webflux.webflux.security.handler;

import com.webflux.webflux.security.dto.CreateUserDTO;
import com.webflux.webflux.security.dto.LoginDTO;
import com.webflux.webflux.security.dto.TokenDTO;
import com.webflux.webflux.security.entity.Users;
import com.webflux.webflux.security.service.UserService;
import com.webflux.webflux.validation.ObjectValidation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
@Slf4j
@RequiredArgsConstructor
public class AuthHandler {
    private final UserService userService;
    private final ObjectValidation objectValidation;
    public  Mono<ServerResponse> login(ServerRequest serverRequest){
        Mono<LoginDTO> loginDTOMono = serverRequest.bodyToMono(LoginDTO.class).doOnNext(objectValidation::validate);
        return loginDTOMono.flatMap(dto -> ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).body(userService.Login(dto), TokenDTO.class));
    }

    public  Mono<ServerResponse> create(ServerRequest serverRequest){
        Mono<CreateUserDTO> loginDTOMono = serverRequest.bodyToMono(CreateUserDTO.class).doOnNext(objectValidation::validate);
        return loginDTOMono.flatMap(dto -> ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).body(userService.create(dto), Users.class));
    }
}

