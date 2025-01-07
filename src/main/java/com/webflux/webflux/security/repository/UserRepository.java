package com.webflux.webflux.security.repository;

import com.webflux.webflux.security.entity.Users;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface UserRepository extends ReactiveCrudRepository<Users, Integer> {
    Mono<Users> findByUsernameOrEmail(String username, String email);
}

