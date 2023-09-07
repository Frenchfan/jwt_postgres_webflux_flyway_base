package com.sumkin.jwt_postgres_webflux_flyway_base.rest;

import com.sumkin.jwt_postgres_webflux_flyway_base.entity.AuthResponse;
import com.sumkin.jwt_postgres_webflux_flyway_base.entity.UserEntity;
import com.sumkin.jwt_postgres_webflux_flyway_base.entity.UserRole;
import com.sumkin.jwt_postgres_webflux_flyway_base.repository.UserRepository;
import com.sumkin.jwt_postgres_webflux_flyway_base.security.CustomPrincipal;
import com.sumkin.jwt_postgres_webflux_flyway_base.security.SecurityService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
@Slf4j
public class AuthRestcontrollerV1 {

    private final SecurityService securityService;
    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private ApplicationContext context;


    @PostMapping("/register")
    public Mono<UserEntity> register(@RequestBody UserEntity user) {
        return userRepository.save(
                user.toBuilder()
                        .password(passwordEncoder.encode(user.getPassword()))
                        .role(UserRole.USER)
                        .enabled(true)
                        .createdAt(LocalDateTime.now())
                        .updatedAt(LocalDateTime.now())
                        .build()
        ).doOnSuccess(u -> log.info("User {} created", u.getEmail()));
    }


    @PostMapping("/login")
    public Mono<AuthResponse> login(@RequestBody UserEntity user) {
        return securityService.authenticate(user.getEmail(), user.getPassword())
                .flatMap(tokenDetails -> Mono.just(
                        AuthResponse.builder()
                                .userId(tokenDetails.getUserId())
                                .token(tokenDetails.getToken())
                                .issuedAt(tokenDetails.getIssuedAt())
                                .expiresAt(tokenDetails.getExpiresAt())
                                .build()
                ));
    }

    @GetMapping("/info")
    public Mono<UserEntity> getUser(Authentication authentication) {
        CustomPrincipal principal = (CustomPrincipal) authentication.getPrincipal();
        return userRepository.findById(principal.getId());
    }

    @GetMapping("/allusers")
    @PreAuthorize("hasAuthority('ADMIN')")
    public Mono<Map<String, Object>> getAllUsers(@RequestParam(defaultValue = "0") Long offset,
                                        @RequestParam(defaultValue = "3") Long limit) {
        Mono<Long> total = userRepository.count();
        Flux<UserEntity> users = userRepository.findAll().skip(offset).take(limit);

        return Mono.zip(total, users.collectList())
                .map(tuple -> {
                    Map<String, Object> result = new HashMap<>();
                    result.put("total", tuple.getT1());
                    result.put("users", tuple.getT2());
                    result.put("count", limit < (tuple.getT1() - offset) ? limit : tuple.getT1() - offset);
                    return result;
                });
    }

    @GetMapping("/myhobbies")
    public Flux<Map<String, String>> getMyHobbies(Authentication authentication) {
        CustomPrincipal principal = (CustomPrincipal) authentication.getPrincipal();
        return userRepository.findMyHobbies(principal.getId())
                .map(hobby -> {
                    Map<String, String> result = new HashMap<>();
                    result.put("name", hobby);
                    return result;
                });
    }


    @GetMapping("/shutdown")
    public void shutdownApp() {
        int exitCode = SpringApplication.exit(context, () -> 0);
        System.exit(exitCode);
    }

}
