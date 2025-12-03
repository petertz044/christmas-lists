package com.zullo.christmas.repository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import com.zullo.christmas.constants.sql.CommonSql;
import com.zullo.christmas.constants.sql.GroupSql;
import com.zullo.christmas.constants.sql.ListSql;
import com.zullo.christmas.model.database.ListEntity;
import com.zullo.christmas.model.database.ListEntry;
import com.zullo.christmas.model.database.User;

@Repository
public class ListRepository {

    Logger LOG = LoggerFactory.getLogger(ListRepository.class);

    NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Autowired
    public ListRepository(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    public boolean createListEntity(ListEntity listEntity) {
        LOG.info("Inserting ListEntity Object {}", listEntity);
        String query = ListSql.CREATE_LIST;
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue(CommonSql.TITLE, listEntity.getTitle());
        params.addValue(CommonSql.DESCRIPTION, listEntity.getDescription());
        params.addValue(CommonSql.USER_ID_OWNER, listEntity.getUserIdOwner());
        params.addValue(CommonSql.USER_ID_LAST_MODIFIED, listEntity.getUserIdOwner());
        return namedParameterJdbcTemplate.update(query, params) > 0;
    }

    public boolean updateListEntity(ListEntity listEntity) {
        LOG.info("Updating GroupMappingList Object {}", listEntity);
        String query = ListSql.UPDATE_LIST_ENTRY;
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue(CommonSql.TITLE, listEntity.getTitle());
        params.addValue(CommonSql.DESCRIPTION, listEntity.getDescription());
        params.addValue(CommonSql.IS_ACTIVE, listEntity.getIsActive());
        params.addValue(CommonSql.USER_ID_LAST_MODIFIED, listEntity.getUserIdOwner());
        return namedParameterJdbcTemplate.update(query, params) > 0;
    }

    public boolean deactivateListEntity(Integer id, User user) {
        LOG.info("Updating GroupMappingUser Object to INACTIVE {}", id);
        String query = ListSql.UPDATE_LIST_ENTITY_INACTIVE;
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue(CommonSql.IS_ACTIVE, false);
        params.addValue(CommonSql.USER_ID_LAST_MODIFIED, user.getId());
        params.addValue(CommonSql.ID, id);
        return namedParameterJdbcTemplate.update(query, params) > 0;
    }

    public ListEntity getListFromId(Integer listId) {
        LOG.info("Entering getListFromId id={}", listId);
        String query = ListSql.SELECT_LIST_BY_ID;
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue(CommonSql.LIST_ID, listId);
        ListEntity list = new ListEntity();
        namedParameterJdbcTemplate.query(query, params, rs -> {
            list.setTitle(rs.getString(CommonSql.TITLE));
            list.setDescription(rs.getString(CommonSql.DESCRIPTION));
            list.setUserIdOwner(rs.getInt(CommonSql.USER_ID_OWNER));
            list.setIsActive(rs.getBoolean(CommonSql.IS_ACTIVE));
            list.setDtCrtd(rs.getObject(CommonSql.DT_CRTD, LocalDateTime.class));
            list.setDtLastModified(rs.getObject(CommonSql.DT_LAST_MODIFIED, LocalDateTime.class));
            list.setUserIdLastModified(rs.getInt(CommonSql.USER_ID_LAST_MODIFIED));
        });
        return list;
    }

    public List<ListEntity> getAllActiveListsForOwner(Integer id) {
        LOG.info("Getting all active lists for owner {}", id);
        String query = ListSql.SELECT_ACTIVE_LISTS_FOR_OWNER;
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue(CommonSql.USER_ID_OWNER, id);
        List<ListEntity> lists = new ArrayList<>();
        namedParameterJdbcTemplate.query(query, params, rs -> {
            ListEntity list = new ListEntity();
            list.setId(rs.getInt(CommonSql.ID));
            list.setTitle(rs.getString(CommonSql.TITLE));
            list.setDescription(rs.getString(CommonSql.DESCRIPTION));
            list.setUserIdOwner(rs.getInt(CommonSql.USER_ID_OWNER));
            list.setIsActive(true);
            list.setDtCrtd(rs.getObject(CommonSql.DT_CRTD, LocalDateTime.class));
            list.setDtLastModified(rs.getObject(CommonSql.DT_LAST_MODIFIED, LocalDateTime.class));
            list.setUserIdLastModified(rs.getInt(CommonSql.USER_ID_LAST_MODIFIED));
            lists.add(list);
        });
        return lists;
    }

    public HashMap<Integer, List<ListEntry>> getAllActiveEntriesForList(List<Integer> ids) {
        LOG.info("Entering getAllActiveEntriesForList ids={}", ids);
        String query = ListSql.SELECT_LIST_ENTRY_BY_LIST_IDS;
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue(CommonSql.LIST_ID, ids);
        HashMap<Integer, List<ListEntry>> listIdToListEntry = new HashMap<>();
        namedParameterJdbcTemplate.query(query, params, rs -> {
            ListEntry entry = new ListEntry();
            entry.setId(rs.getInt(CommonSql.ID));
            entry.setUrl(rs.getString(CommonSql.URL));
            entry.setTitle(rs.getString(CommonSql.TITLE));
            entry.setDescription(rs.getString(CommonSql.DESCRIPTION));
            entry.setPriority(rs.getString(CommonSql.PRIORITY).charAt(0));
            entry.setIsActive(true);
            entry.setUserIdOwner(rs.getInt(CommonSql.USER_ID_OWNER));
            entry.setListId(rs.getInt(CommonSql.LIST_ID));
            entry.setIsPurchased(rs.getBoolean(CommonSql.IS_PURCHASED));
            entry.setUserIdPurchased(rs.getInt(CommonSql.USER_ID_PURCHASED));
            entry.setDtCrtd(rs.getObject(CommonSql.DT_CRTD, LocalDateTime.class));
            entry.setDtLastModified(rs.getObject(CommonSql.DT_LAST_MODIFIED, LocalDateTime.class));
            entry.setUserIdLastModified(rs.getInt(CommonSql.USER_ID_LAST_MODIFIED));
            if (listIdToListEntry.containsKey(entry.getListId())) {
                listIdToListEntry.get(entry.getListId()).add(entry);
            } else {
                List<ListEntry> list = new ArrayList<>();
                list.add(entry);
                listIdToListEntry.put(entry.getListId(), list);
            }
        });
        return listIdToListEntry;
    }

    public boolean createListEntry(ListEntry entry) {
        LOG.info("Inserting ListEntry Object {}", entry);
        String query = ListSql.CREATE_LIST_ENTRY;
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue(CommonSql.URL, entry.getUrl());
        params.addValue(CommonSql.TITLE, entry.getTitle());
        params.addValue(CommonSql.DESCRIPTION, entry.getDescription());
        params.addValue(CommonSql.PRIORITY, entry.getPriority());
        params.addValue(CommonSql.LIST_ID, entry.getListId());
        params.addValue(CommonSql.USER_ID_OWNER, entry.getUserIdOwner());
        params.addValue(CommonSql.IS_PURCHASED, entry.getIsPurchased());
        params.addValue(CommonSql.USER_ID_LAST_MODIFIED, entry.getUserIdOwner());
        return namedParameterJdbcTemplate.update(query, params) > 0;
    }

    public boolean updateListEntry(ListEntry entry, User requestor) {
        LOG.info("Updating GroupMappingList Object {}", entry);
        String query = ListSql.UPDATE_LIST_ENTRY;
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue(CommonSql.URL, entry.getUrl());
        params.addValue(CommonSql.TITLE, entry.getTitle());
        params.addValue(CommonSql.DESCRIPTION, entry.getDescription());
        params.addValue(CommonSql.PRIORITY, entry.getPriority());
        params.addValue(CommonSql.IS_PURCHASED, entry.getIsPurchased());
        params.addValue(CommonSql.USER_ID_PURCHASED, entry.getUserIdPurchased());
        params.addValue(CommonSql.IS_ACTIVE, entry.getIsActive());
        params.addValue(CommonSql.USER_ID_LAST_MODIFIED, requestor.getId());
        params.addValue(CommonSql.ID, entry.getId());
        params.addValue(CommonSql.LIST_ID, entry.getListId());
        return namedParameterJdbcTemplate.update(query, params) > 0;
    }

    public boolean deactivateListEntry(Integer id, User user) {
        LOG.info("Updating GroupMappingUser Object to INACTIVE {}", id);
        String query = ListSql.UPDATE_LIST_ENTRY_INACTIVE;
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue(CommonSql.IS_ACTIVE, false);
        params.addValue(CommonSql.USER_ID_LAST_MODIFIED, user.getId());
        params.addValue(CommonSql.ID, id);
        return namedParameterJdbcTemplate.update(query, params) > 0;
    }

    public List<ListEntity> getAllActiveListsForGroup(List<Integer> ids) {
        LOG.info("Getting all active lists for groups {}", ids);
        String query = GroupSql.SELECT_ACTIVE_LISTS_FOR_GROUP_IDS;
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue(CommonSql.GROUP_ID, ids);
        List<ListEntity> lists = new ArrayList<>();
        namedParameterJdbcTemplate.query(query, params, rs -> {
            ListEntity list = new ListEntity();
            list.setId(rs.getInt(CommonSql.LIST_ID));
            list.setTitle(rs.getString(CommonSql.TITLE));
            list.setDescription(rs.getString(CommonSql.DESCRIPTION));
            list.setUserIdOwner(rs.getInt(CommonSql.USER_ID_OWNER));
            lists.add(list);
        });
        return lists;
    }
}
