package com.zullo.christmas.repository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import com.zullo.christmas.model.Database.User;

@Repository
public class UserRepository {
    Logger LOG = LoggerFactory.getLogger(UserRepository.class);

    NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Autowired
    public UserRepository(NamedParameterJdbcTemplate namedParameterJdbcTemplate){
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }


    public User getUserByUsername(String username){
        String query = """
                SELECT U.username, U.password FROM "user" U WHERE U.username = :username
                """;
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("username", username);
        User user = new User();
        namedParameterJdbcTemplate.query(query, params, rs -> {
            user.setUsername(rs.getString("username"));
            user.setPassword(rs.getString("password"));
        });


        return user;
    }

    public boolean insertUser(User user){

        String query = """
                INSERT INTO "user"(
                    username,
                    password,
                    role,
                    dt_crtd,
                    dt_last_modified
                )
                VALUES(
                    :username,
                    :password,
                    :role,
                    timezone('utc', now()),
                    timezone('utc', now())
                )
                """;
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("username", user.getUsername());
        params.addValue("password", user.getPassword());
        params.addValue("role", user.getRole());
        return namedParameterJdbcTemplate.update(query, params) > 0;
    }
}
