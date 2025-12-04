package com.zullo.christmas.constants.sql;

public class UserSql {
    public static final String SELECT_USER_BY_USERNAME = String.format("""
            SELECT 
                U.id, 
                U.username, 
                U.password, 
                U.role, 
                U.user_id_last_modified 
            FROM "user" U WHERE U.username = :%s
            """, CommonSql.USERNAME);
    public static final String SELECT_USER_BY_ID = String.format("""
            SELECT 
                U.id, 
                U.username, 
                U.password, 
                U.role, 
                U.user_id_last_modified 
            FROM "user" U WHERE U.id = :%s
            """, CommonSql.ID);

    public static final String INSERT_USER = String.format("""
                INSERT INTO "user"(
                    username,
                    password,
                    role,
                    dt_crtd,
                    dt_last_modified
                )
                VALUES(
                    :%s,
                    :%s,
                    :%s,
                    timezone('utc', now()),
                    timezone('utc', now())
                )
                """, CommonSql.USERNAME, CommonSql.PASSWORD, CommonSql.ROLE);
    public static final String UPDATE_USER_AUDIT = String.format("""
            UPDATE
                "user" u
            SET
                dt_last_login = timezone('utc', now())
            WHERE
                u.username =:%s;
            """, CommonSql.USERNAME);

    public static final String UPDATE_USER_ROLE = String.format("""
            UPDATE
                "user" u
            SET
                role = :%s
                user_id_last_modified = :%s
                dt_last_modified = timezone('utc', now())
            WHERE
                u.id =:%s;
            """, CommonSql.ROLE, CommonSql.USER_ID_LAST_MODIFIED, CommonSql.ID);
}
