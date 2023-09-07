package com.sumkin.jwt_postgres_webflux_flyway_base.security;

import com.sumkin.jwt_postgres_webflux_flyway_base.entity.UserEntity;
import com.sumkin.jwt_postgres_webflux_flyway_base.repository.UserRepository;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.*;

@Component
@RequiredArgsConstructor
public class SecurityService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Value("${jwt.secret}")
    private String secret;
    @Value("${jwt.expiration}")
    private Integer expirationInSeconds;
    @Value("${jwt.issuer}")
    private String issuer;

    /**
     * Generates a token for the given user.
     *
     * @param  user  the user entity for which to generate the token
     * @return       the token details containing the generated token
     */
    private TokenDetails generateToken(UserEntity user) {
        Map<String, Object> claims = new HashMap<>() {{
            put("role", user.getRole());
            put("email", user.getEmail());

        }};
        return generateToken(claims, user.getId().toString());

    }

    /**
     * Generates a token with the given claims and subject.
     *
     * @param  claims  a map containing the claims for the token
     * @param  subject the subject of the token
     * @return         the generated TokenDetails object
     */
    private TokenDetails generateToken(Map<String, Object> claims, String subject) {
        Long expirationTimeInMillis = expirationInSeconds * 1000L;
        Date expirationDate = new Date(new Date().getTime() + expirationTimeInMillis);

        return generateToken(expirationDate, claims, subject);
    }

    /**
     * Generates a token with the specified expiration date, claims, and subject.
     *
     * @param  expirationDate  the expiration date of the token
     * @param  claims          the claims to include in the token
     * @param  subject         the subject of the token
     * @return                 the generated token details
     */
    private TokenDetails generateToken(Date expirationDate, Map<String, Object> claims, String subject) {
        Date createdDate = new Date();
        String token = Jwts.builder()
                .setClaims(claims)
                .setIssuer(issuer)
                .setSubject(subject)
                .setIssuedAt(createdDate)
                .setId(UUID.randomUUID().toString())
                .setExpiration(expirationDate)
                .signWith(SignatureAlgorithm.HS256, Base64.getEncoder().encodeToString(secret.getBytes()))
                .compact();

        return TokenDetails.builder()
                .token(token)
                .issuedAt(createdDate)
                .expiresAt(expirationDate)
                .build();
    }


    /**
     * Authenticates the user with the given email and password.
     *
     * @param  email  the email of the user
     * @param  password  the password of the user
     * @return           the details of the authenticated user
     */
    public Mono<TokenDetails> authenticate(String email, String password) {
        return userRepository.findByEmail(email)
                .flatMap(user -> {
                    if (!user.isEnabled()) {
                        return Mono.error(new RuntimeException("User not enabled"));
                    }
                    if (!passwordEncoder.matches(password, user.getPassword())) {
                        return Mono.error(new RuntimeException("Invalid password"));
                    }
                    return Mono.just(generateToken(user).toBuilder()
                            .userId(user.getId())
                            .build());
                })
                .switchIfEmpty(Mono.error(new RuntimeException("User not found")));
    }


}
