package com.zullo.christmas.repository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import com.zullo.christmas.constants.sql.UserSql;
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
        LOG.info("Getting User Object for username {}", username);
        String query = UserSql.SELECT_USER_BY_USERNAME;
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue(UserSql.USERNAME, username);
        User user = new User();
        namedParameterJdbcTemplate.query(query, params, rs -> {
            user.setId(rs.getInt(UserSql.ID));
            user.setUsername(rs.getString(UserSql.USERNAME));
            user.setPassword(rs.getString(UserSql.PASSWORD));
            user.setRole(rs.getString(UserSql.ROLE));
        });
        return user;
    }

    public User getUserById(Integer id){
        LOG.info("Getting User Object for id {}", id);
        String query = UserSql.SELECT_USER_BY_ID;
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue(UserSql.ID, id);
        User user = new User();
        namedParameterJdbcTemplate.query(query, params, rs -> {
            user.setId(rs.getInt(UserSql.ID));
            user.setUsername(rs.getString(UserSql.USERNAME));
            user.setPassword(rs.getString(UserSql.PASSWORD));
            user.setRole(rs.getString(UserSql.ROLE));
        });
        return user;
    }


    public boolean insertUser(User user){
        LOG.info("Inserting User Object {}", user.toBuilder().password("REDACTED").build());
        String query = UserSql.INSERT_USER;
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue(UserSql.USERNAME, user.getUsername());
        params.addValue(UserSql.PASSWORD, user.getPassword());
        params.addValue(UserSql.ROLE, user.getRole());
        return namedParameterJdbcTemplate.update(query, params) > 0;
    }

    public boolean updateUserAfterLogin(User user){
        LOG.info("Updating User Object after login {}", user.toBuilder().password("REDACTED").build());
        String query = UserSql.UPDATE_USER_AUDIT;
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue(UserSql.USERNAME, user.getUsername());
        return namedParameterJdbcTemplate.update(query, params) == 1;
    }

    public boolean updateUserRole(String target, Integer adminId, String role){
        LOG.info("Updating User Role for user: {} to role {}", target, role);
        String query = UserSql.UPDATE_USER_ROLE;
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue(UserSql.ROLE, role);
        params.addValue(UserSql.USERNAME, target);
        params.addValue(UserSql.USER_ID_LAST_MODIFIED, adminId);
        return namedParameterJdbcTemplate.update(query, params) == 1;
    }
}
