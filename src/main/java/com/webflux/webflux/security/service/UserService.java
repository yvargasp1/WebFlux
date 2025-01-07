package com.webflux.webflux.security.service;

import com.webflux.webflux.exception.CustomException;
import com.webflux.webflux.security.dto.CreateUserDTO;
import com.webflux.webflux.security.dto.LoginDTO;
import com.webflux.webflux.security.dto.TokenDTO;
import com.webflux.webflux.security.entity.Users;
import com.webflux.webflux.security.enums.Role;
import com.webflux.webflux.security.jwt.JwtProvider;
import com.webflux.webflux.security.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final JwtProvider jwtProvider;
    private final PasswordEncoder passwordEncoder;

    public Mono<TokenDTO> Login(LoginDTO loginDTO) {
        return userRepository.findByUsernameOrEmail(loginDTO.getUsername(), loginDTO.getUsername())
                .filter(users -> passwordEncoder.matches(loginDTO.getPassword(), users.getPassword()))
                .map(users -> new TokenDTO(jwtProvider.generateToken(users)))
                .switchIfEmpty(Mono.error(new CustomException(HttpStatus.BAD_REQUEST, "bad credentials")));
    }

    public Mono<Users> create(CreateUserDTO createUserDTO) {
        String roles = Arrays.stream(createUserDTO.getRoles()).map(rol ->
                {
                    if (rol.equalsIgnoreCase("ADMIN")) {
                        return Role.ROLE_ADMIN.name();
                    } else if (rol.equalsIgnoreCase("USER")) {
                        return Role.ROLE_USER.name();
                    } else {
                        throw new IllegalArgumentException("Invalid Role" + rol);
                    }
                }
        ).collect(Collectors.joining(", "));
        Users users = Users.builder().username(createUserDTO.getUsername()).email(createUserDTO.getEmail()).password(passwordEncoder.encode(createUserDTO.getPassword())).roles(roles).build();
        Mono<Boolean> userExists = userRepository.findByUsernameOrEmail(users.getUsername(), users.getEmail()).hasElement();
        return userExists.flatMap(exists -> exists
                ? Mono.error(new CustomException(HttpStatus.BAD_REQUEST, "username or email already in use"))
                : userRepository.save(users)
        );
    }
}
