package com.zullo.christmas.constants.sql;

public class ListSql {
    public static final String CREATE_LIST = String.format("""
            INSERT INTO "list"(
                title,
                description,
                user_id_owner,
                is_active,
                dt_crtd,
                dt_last_modified,
                user_id_last_modified
            )
            VALUES(
                :%s,
                :%s,
                :%s,
                true,
                timezone('utc', now()),
                timezone('utc', now()),
                :%s
            )
            """, CommonSql.TITLE, CommonSql.DESCRIPTION, CommonSql.USER_ID_OWNER, CommonSql.USER_ID_LAST_MODIFIED);

    public static final String UPDATE_LIST = String.format("""
            UPDATE list SET
                title=:%s,
                description=:%s,
                is_active=:%s,
                dt_last_modified=timezone('utc', now()),
                user_id_last_modified=:%s
            WHERE
                id=:%s
            """, CommonSql.TITLE, CommonSql.DESCRIPTION, CommonSql.IS_ACTIVE, CommonSql.USER_ID_LAST_MODIFIED,
            CommonSql.ID);

    public static final String CREATE_LIST_ENTRY = String.format("""
            INSERT INTO "list_entry"(
                url,
                title,
                description,
                priority,
                list_id,
                user_id_owner,
                is_purchased,
                is_active,
                dt_crtd,
                dt_last_modified,
                user_id_last_modified
            )
            VALUES(
                :%s,
                :%s,
                :%s,
                :%s,
                :%s,
                :%s,
                :%s,
                true,
                timezone('utc', now()),
                timezone('utc', now()),
                :%s
            )
            """, CommonSql.URL, CommonSql.TITLE, CommonSql.DESCRIPTION, CommonSql.PRIORITY, CommonSql.LIST_ID,
            CommonSql.USER_ID_OWNER, CommonSql.IS_PURCHASED, CommonSql.USER_ID_LAST_MODIFIED);

    public static final String UPDATE_LIST_ENTRY = String.format("""
            UPDATE "list_entry" SET
                url=:%s,
                title=:%s,
                description=:%s,
                priority=:%s,
                is_purchased=:%s,
                user_id_purchased=:%s,
                is_active=:%s,
                dt_last_modified=timezone('utc', now()),
                user_id_last_modified=:%s
            WHERE
                id=:%s AND
                list_id=:%s
            """, CommonSql.URL, CommonSql.TITLE, CommonSql.DESCRIPTION, CommonSql.PRIORITY, CommonSql.IS_PURCHASED,
            CommonSql.USER_ID_PURCHASED, CommonSql.IS_ACTIVE, CommonSql.USER_ID_LAST_MODIFIED, CommonSql.ID,
            CommonSql.LIST_ID);

    public static final String SELECT_LIST_ENTRY_BY_LIST_IDS = String.format("""
            SELECT
                id,
                url,
                title,
                description,
                priority,
                list_id,
                user_id_owner,
                is_purchased,
                user_id_purchased,
                dt_crtd,
                dt_last_modified,
                user_id_last_modified
            FROM
                list_entry
            WHERE
                list_id IN (:%s) AND
                is_active = true
            """, CommonSql.LIST_ID);

    public static final String SELECT_LIST_ENTRY_BY_ENTRY_ID = String.format("""
            SELECT
                url,
                title,
                description,
                priority,
                list_id,
                user_id_owner,
                is_purchased,
                user_id_purchased,
                dt_crtd,
                dt_last_modified,
                user_id_last_modified
            FROM
                list_entry
            WHERE
                id IN (:%s) AND
                is_active = true
            """, CommonSql.ID);

    public static final String SELECT_LIST_BY_ID = String.format("""
            SELECT
                title,
                description,
                user_id_owner,
                is_active,
                dt_crtd,
                dt_last_modified,
                user_id_last_modified
            FROM
                "list"
            WHERE
                id=:%s
            """, CommonSql.LIST_ID);

    public static final String SELECT_ACTIVE_LISTS_FOR_OWNER = String.format("""
            SELECT
                id,
                title,
                description,
                user_id_owner,
                dt_crtd,
                dt_last_modified,
                user_id_last_modified
            FROM
                "list"
            WHERE
                is_active=true AND
                user_id_owner=:%s
            """, CommonSql.USER_ID_OWNER);

    public static final String UPDATE_LIST_ENTITY_INACTIVE = String.format("""
            UPDATE "list" SET
                is_active=:%s,
                dt_last_modified=timezone('utc', now()),
                user_id_last_modified=:%s
            WHERE
                id=:%s
            """, CommonSql.IS_ACTIVE, CommonSql.USER_ID_LAST_MODIFIED, CommonSql.ID);

    public static final String UPDATE_LIST_ENTRY_INACTIVE = String.format("""
            UPDATE list_entry SET
                is_active=:%s,
                dt_last_modified=timezone('utc', now()),
                user_id_last_modified=:%s
            WHERE
                id=:%s
            """, CommonSql.IS_ACTIVE, CommonSql.USER_ID_LAST_MODIFIED, CommonSql.ID);

};
