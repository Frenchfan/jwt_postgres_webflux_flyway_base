package com.sumkin.jwt_postgres_webflux_flyway_base;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMethod;

@SpringBootApplication
public class JwtPostgresWebfluxFlywayBaseApplication {

	public static void main(String[] args) {
		SpringApplication.run(JwtPostgresWebfluxFlywayBaseApplication.class, args);
	}

	//http://localhost:8084/webjars/swagger-ui/index.html
	//https://www.grc.com/passwords.htm
}
