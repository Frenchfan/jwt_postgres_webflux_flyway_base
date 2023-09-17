package com.sumkin.jwt_postgres_webflux_flyway_base.rest;

import com.sumkin.jwt_postgres_webflux_flyway_base.entity.AuthResponse;
import com.sumkin.jwt_postgres_webflux_flyway_base.security.SecurityService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@Controller
@CrossOrigin(origins = "*", allowedHeaders = "*")
// It is important to annotate the controller with @Controller as it is the only way to use templates
@RequestMapping("/api/loginextra")
public class ExtraController {

    private final SecurityService securityService;

    public ExtraController(SecurityService securityService) {
        this.securityService = securityService;
    }


    @GetMapping

    public String showLoginPage() {
        return "loginPage1";
    }


    @PostMapping("/auth")
    @ResponseBody
    public Mono<AuthResponse> login(@RequestParam("email") String email, @RequestParam("password") String password) {
        return securityService.authenticate(email, password)
                .flatMap(tokenDetails -> Mono.just(
                        AuthResponse.builder()
                                .userId(tokenDetails.getUserId())
                                .token(tokenDetails.getToken())
                                .issuedAt(tokenDetails.getIssuedAt())
                                .expiresAt(tokenDetails.getExpiresAt())
                                .build()
                ));
    }
}
