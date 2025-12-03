package com.zullo.christmas.constants.sql;

public class GroupSql {
    public static final String CREATE_GROUP = String.format("""
            INSERT INTO "group"(
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

    public static final String UPDATE_GROUP = String.format("""
            UPDATE "group" SET
                title=:%s,
                description=:%s,
                is_active=:%s,
                dt_last_modified=timezone('utc', now()),
                user_id_last_modified=:%s
            WHERE
                id=:%s
            """, CommonSql.TITLE, CommonSql.DESCRIPTION, CommonSql.IS_ACTIVE, CommonSql.USER_ID_LAST_MODIFIED,
            CommonSql.ID);

    public static final String CREATE_GROUP_MAPPING_USER = String.format("""
            INSERT INTO "group_mapping_user" (
                group_id,
                user_id,
                is_active,
                dt_crtd,
                dt_last_modified,
                user_id_last_modified
            )
            VALUES (
                :%s,
                :%s,
                true,
                timezone('utc', now()),
                timezone('utc', now()),
                :%s
            )
            """, CommonSql.GROUP_ID, CommonSql.USER_ID, CommonSql.USER_ID_LAST_MODIFIED);

    public static final String UPDATE_GROUP_MAPPING_USER = String.format("""
            UPDATE group_mapping_user SET
                group_id=:%s,
                user_id=:%s,
                is_active=:%s,
                dt_last_modified=timezone('utc', now()),
                user_id_last_modified=:%s
            WHERE
                id=:%s
            """, CommonSql.GROUP_ID, CommonSql.USER_ID, CommonSql.IS_ACTIVE, CommonSql.USER_ID_LAST_MODIFIED,
            CommonSql.ID);

    public static final String CREATE_GROUP_MAPPING_LIST = String.format("""
            INSERT INTO "group_mapping_list" (
                group_id,
                list_id,
                is_active,
                dt_crtd,
                dt_last_modified,
                user_id_last_modified
            )
            VALUES (
                :%s,
                :%s,
                true,
                timezone('utc', now()),
                timezone('utc', now()),
                :%s
            )
            """, CommonSql.GROUP_ID, CommonSql.LIST_ID, CommonSql.USER_ID_LAST_MODIFIED);

    public static final String UPDATE_GROUP_MAPPING_LIST = String.format("""
            UPDATE group_mapping_list SET
                group_id=:%s,
                list_id=:%s,
                is_active=:%s,
                dt_last_modified=timezone('utc', now()),
                user_id_last_modified=:%s
            WHERE
                id=:%s
            """, CommonSql.GROUP_ID, CommonSql.LIST_ID, CommonSql.IS_ACTIVE, CommonSql.USER_ID_LAST_MODIFIED,
            CommonSql.ID);

    public static final String UPDATE_GROUP_MAPPING_LIST_INACTIVE = String.format("""
            UPDATE group_mapping_list SET
                is_active=:%s,
                dt_last_modified=timezone('utc', now()),
                user_id_last_modified=:%s
            WHERE
                id=:%s
            """, CommonSql.IS_ACTIVE, CommonSql.USER_ID_LAST_MODIFIED, CommonSql.ID);

    public static final String UPDATE_GROUP_MAPPING_USER_INACTIVE = String.format("""
            UPDATE group_mapping_user SET
                is_active=:%s,
                dt_last_modified=timezone('utc', now()),
                user_id_last_modified=:%s
            WHERE
                id=:%s
            """, CommonSql.IS_ACTIVE, CommonSql.USER_ID_LAST_MODIFIED, CommonSql.ID);
            
    public static final String UPDATE_GROUP_INACTIVE = String.format("""
            UPDATE "group" SET
                is_active=:%s,
                dt_last_modified=timezone('utc', now()),
                user_id_last_modified=:%s
            WHERE
                id=:%s
            """, CommonSql.IS_ACTIVE, CommonSql.USER_ID_LAST_MODIFIED, CommonSql.ID);

    public static final String SELECT_ACTIVE_GROUPS_FOR_USER_ID = String.format("""
            SELECT
                id,
                group_id,
                dt_crtd,
                dt_last_modified,
                user_id_last_modified
            FROM
                group_mapping_user
            WHERE
                user_id=:%s AND
                is_active=true
            """, CommonSql.USER_ID);

    public static final String SELECT_ACTIVE_LISTS_FOR_GROUP_IDS = String.format("""
            SELECT
                gml.list_id
                l.title,
                l.description,
                l.user_id_owner,
            FROM
                group_mapping_list gml
            LEFT JOIN "list" l
                ON gml.list_id=l.list_id
            WHERE
                gml.group_id IN (:%s) AND
                gml.is_active=true
                l.is_active=true
            """, CommonSql.GROUP_ID);

    public static final String SELECT_ACTIVE_LISTS_FOR_OWNER = String.format("""
            SELECT
                list_id,
                title,
                description
            FROM
                "list"
            WHERE
                is_active=true AND
                user_id_owner=:%s
            """, CommonSql.USER_ID_OWNER);

    public static final String SELECT_ACTIVE_GROUPS_FOR_LIST_ID = String.format("""
            SELECT
                group_id
            FROM
                group_mapping_list
            WHERE
                list_id = :%s
            """, CommonSql.LIST_ID);
    public static final String SELECT_ALL_GROUPS = """
            SELECT 
                id,
                title,
                description,
                user_id_owner,
                is_active,
                dt_crtd,
                dt_last_modified,
                user_id_last_modified
            FROM 
                "group"
            WHERE
                is_active=true
            """;
}
