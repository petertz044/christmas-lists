package com.zullo.christmas.constants.sql;

public class GroupSQL {
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
            """, Common.TITLE, Common.DESCRIPTION, Common.USER_ID_OWNER, Common.USER_ID_LAST_MODIFIED);

    public static final String UPDATE_GROUP = String.format("""
            UPDATE list SET
                title=:%s,
                description=:%s,
                user_id_owner=:%s,
                is_active=:%s,
                dt_last_modified=timezone('utc', now()),
                user_id_last_modified=:%s
            WHERE
                id=:%s
            """, Common.TITLE, Common.DESCRIPTION, Common.USER_ID_OWNER, Common.IS_ACTIVE, Common.USER_ID_LAST_MODIFIED,
            Common.ID);

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
            """, Common.GROUP_ID, Common.USER_ID, Common.USER_ID_LAST_MODIFIED);

    public static final String UPDATE_GROUP_MAPPING_USER = String.format("""
            UPDATE group_mapping_user SET
                group_id=:%s,
                user_id=:%s,
                is_active=:%s,
                dt_last_modified=timezone('utc', now()),
                user_id_last_modified=:%s
            WHERE 
                id=:%s
            """, Common.GROUP_ID, Common.USER_ID, Common.IS_ACTIVE, Common.USER_ID_LAST_MODIFIED, Common.ID);

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
            """, Common.GROUP_ID, Common.LIST_ID, Common.USER_ID_LAST_MODIFIED);

    public static final String UPDATE_GROUP_MAPPING_LIST = String.format("""
            UPDATE group_mapping_list SET
                group_id=:%s,
                list_id=:%s,
                is_active=:%s,
                dt_last_modified=timezone('utc', now()),
                user_id_last_modified=:%s
            WHERE 
                id=:%s
            """, Common.GROUP_ID, Common.LIST_ID, Common.IS_ACTIVE, Common.USER_ID_LAST_MODIFIED, Common.ID);

    public static final String UPDATE_GROUP_MAPPING_LIST_INACTIVE = String.format("""
            UPDATE group_mapping_list SET
                is_active=:%s,
                dt_last_modified=timezone('utc', now()),
                user_id_last_modified=:%s
            WHERE 
                id=:%s
            """, Common.IS_ACTIVE, Common.USER_ID_LAST_MODIFIED, Common.ID);

    public static final String UPDATE_GROUP_MAPPING_USER_INACTIVE = String.format("""
            UPDATE group_mapping_user SET
                is_active=:%s,
                dt_last_modified=timezone('utc', now()),
                user_id_last_modified=:%s
            WHERE 
                id=:%s
            """, Common.IS_ACTIVE, Common.USER_ID_LAST_MODIFIED, Common.ID);

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
            """, Common.USER_ID);

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
            """, Common.GROUP_ID);

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
            """, Common.USER_ID_OWNER);

    public static final String SELECT_ACTIVE_GROUPS_FOR_LIST_ID = String.format("""
            SELECT 
                group_id
            FROM
                group_mapping_list
            WHERE
                list_id = :%s
            """, Common.LIST_ID);
    
}
