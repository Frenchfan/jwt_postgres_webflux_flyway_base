package com.sumkin.jwt_postgres_webflux_flyway_base.security;

import com.sumkin.jwt_postgres_webflux_flyway_base.entity.UserEntity;
import com.sumkin.jwt_postgres_webflux_flyway_base.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class AuthenticationManager implements ReactiveAuthenticationManager {

    private final UserRepository userRepository;
    @Override
    public Mono<Authentication> authenticate(Authentication authentication) {
        CustomPrincipal principal = (CustomPrincipal) authentication.getPrincipal();

        return userRepository.findById(principal.getId())
                .filter(UserEntity::isEnabled)
                .switchIfEmpty(Mono.error(new RuntimeException("User disabled")))
                .map(user -> authentication);
    }
}
