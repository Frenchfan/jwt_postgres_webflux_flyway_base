package com.sumkin.jwt_postgres_webflux_flyway_base.repository;

import com.sumkin.jwt_postgres_webflux_flyway_base.entity.UserEntity;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Map;

public interface UserRepository extends R2dbcRepository<UserEntity, Long> {

    Mono<UserEntity> findByEmail(String username);
    @Query("SELECT name \n" +
            "FROM hobbies \n" +
            "    JOIN user_hobbies on hobbies.id = user_hobbies.hobby_id\n" +
            "    JOIN users on user_hobbies.user_id = users.id\n" +
            "WHERE users.id = :userId;")
    Flux<String> findMyHobbies(Long userId);


}
