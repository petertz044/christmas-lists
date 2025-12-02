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
            """, Common.TITLE, Common.DESCRIPTION, Common.USER_ID_OWNER, Common.USER_ID_LAST_MODIFIED);

    public static final String UPDATE_LIST = String.format("""
            UPDATE list SET
                title=:%s,
                description=:%s,
                is_active=:%s,
                dt_last_modified=timezone('utc', now()),
                user_id_last_modified=:%s
            WHERE
                id=:%s
            """, Common.TITLE, Common.DESCRIPTION, Common.IS_ACTIVE, Common.USER_ID_LAST_MODIFIED, Common.ID);

    public static final String CREATE_LIST_ENTRY = String.format("""
            INSERT INTO "list_entry"(
                url,
                title,
                description,
                priority,
                listId,
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
            """, Common.URL, Common.TITLE, Common.DESCRIPTION, Common.PRIORITY, Common.LIST_ID,
            Common.USER_ID_OWNER, Common.IS_PURCHASED, Common.USER_ID_LAST_MODIFIED);

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
            """, Common.URL, Common.TITLE, Common.DESCRIPTION, Common.PRIORITY, Common.IS_PURCHASED,
            Common.USER_ID_PURCHASED, Common.IS_ACTIVE, Common.USER_ID_LAST_MODIFIED, Common.ID, Common.LIST_ID);

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
                list_id IN :%s AND
                is_active = true
            """, Common.LIST_ID);

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
            """, Common.LIST_ID);

    public static final String UPDATE_LIST_ENTITY_INACTIVE = String.format("""
            UPDATE "list" SET
                is_active=:%s,
                dt_last_modified=timezone('utc', now()),
                user_id_last_modified=:%s
            WHERE
                id=:%s
            """, Common.IS_ACTIVE, Common.USER_ID_LAST_MODIFIED, Common.ID);

    public static final String UPDATE_LIST_ENTRY_INACTIVE = String.format("""
            UPDATE list_entry SET
                is_active=:%s,
                dt_last_modified=timezone('utc', now()),
                user_id_last_modified=:%s
            WHERE
                id=:%s
            """, Common.IS_ACTIVE, Common.USER_ID_LAST_MODIFIED, Common.ID);
};
