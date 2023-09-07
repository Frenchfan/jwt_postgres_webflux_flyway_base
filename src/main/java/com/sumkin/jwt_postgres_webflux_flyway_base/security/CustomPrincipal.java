package com.sumkin.jwt_postgres_webflux_flyway_base.security;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.security.Principal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustomPrincipal implements Principal {

    private Long id;
    private String name;

}
