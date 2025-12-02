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

import com.zullo.christmas.constants.sql.Common;
import com.zullo.christmas.constants.sql.GroupSQL;
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
        params.addValue(Common.TITLE, listEntity.getTitle());
        params.addValue(Common.DESCRIPTION, listEntity.getDescription());
        params.addValue(Common.USER_ID_OWNER, listEntity.getUserIdOwner());
        params.addValue(Common.USER_ID_LAST_MODIFIED, listEntity.getUserIdOwner());
        return namedParameterJdbcTemplate.update(query, params) > 0;
    }

    public boolean updateListEntity(ListEntity listEntity) {
        LOG.info("Updating GroupMappingList Object {}", listEntity);
        String query = ListSql.UPDATE_LIST_ENTRY;
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue(Common.TITLE, listEntity.getTitle());
        params.addValue(Common.DESCRIPTION, listEntity.getDescription());
        params.addValue(Common.IS_ACTIVE, listEntity.getIsActive());
        params.addValue(Common.USER_ID_LAST_MODIFIED, listEntity.getUserIdOwner());
        return namedParameterJdbcTemplate.update(query, params) > 0;
    }

    public boolean deactivateListEntity(Integer id, User user) {
        LOG.info("Updating GroupMappingUser Object to INACTIVE {}", id);
        String query = ListSql.UPDATE_LIST_ENTRY_INACTIVE;
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue(Common.IS_ACTIVE, false);
        params.addValue(Common.USER_ID_LAST_MODIFIED, user.getId());
        params.addValue(Common.ID, id);
        return namedParameterJdbcTemplate.update(query, params) > 0;
    }

    public ListEntity getListFromId(Integer listId) {
        LOG.info("Entering getListFromId id={}", listId);
        String query = ListSql.SELECT_LIST_BY_ID;
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue(Common.LIST_ID, listId);
        ListEntity list = new ListEntity();
        namedParameterJdbcTemplate.query(query, params, rs -> {
            list.setTitle(rs.getString(Common.TITLE));
            list.setDescription(rs.getString(Common.DESCRIPTION));
            list.setUserIdOwner(rs.getInt(Common.USER_ID_OWNER));
            list.setIsActive(rs.getBoolean(Common.IS_ACTIVE));
            list.setDtCrtd(rs.getObject(Common.DT_CRTD, LocalDateTime.class));
            list.setDtLastModified(rs.getObject(Common.DT_LAST_MODIFIED, LocalDateTime.class));
            list.setUserIdLastModified(rs.getInt(Common.USER_ID_LAST_MODIFIED));
        });
        return list;
    }

    public List<ListEntity> getAllActiveListsForOwner(Integer id) {
        LOG.info("Getting all active lists for owner {}", id);
        String query = GroupSQL.SELECT_ACTIVE_LISTS_FOR_OWNER;
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue(Common.USER_ID_OWNER, id);
        List<ListEntity> lists = new ArrayList<>();
        namedParameterJdbcTemplate.query(query, params, rs -> {
            ListEntity list = new ListEntity();
            list.setId(rs.getInt(Common.LIST_ID));
            list.setTitle(rs.getString(Common.TITLE));
            list.setDescription(rs.getString(Common.DESCRIPTION));
            lists.add(list);
        });
        return lists;
    }

    public List<List<ListEntry>> getAllActiveEntriesForList(List<Integer> ids) {
        LOG.info("Entering getAllActiveEntriesForList ids={}", ids);
        String query = ListSql.SELECT_LIST_ENTRY_BY_LIST_IDS;
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue(Common.LIST_ID, ids);
        List<List<ListEntry>> listEntries = new ArrayList<>();
        HashMap<Integer, List<ListEntry>> listIdToListEntry = new HashMap<>();
        namedParameterJdbcTemplate.query(query, params, rs -> {
            ListEntry entry = new ListEntry();
            entry.setId(rs.getInt(Common.ID));
            entry.setUrl(rs.getString(Common.URL));
            entry.setTitle(rs.getString(Common.TITLE));
            entry.setDescription(rs.getString(Common.DESCRIPTION));
            entry.setPriority(rs.getString(Common.PRIORITY).charAt(0));
            entry.setUserIdOwner(rs.getInt(Common.USER_ID_OWNER));
            entry.setListId(rs.getInt(Common.LIST_ID));
            entry.setIsPurchased(rs.getBoolean(Common.IS_PURCHASED));
            entry.setUserIdPurchased(rs.getInt(Common.USER_ID_PURCHASED));
            entry.setDtCrtd(rs.getObject(Common.DT_CRTD, LocalDateTime.class));
            entry.setDtLastModified(rs.getObject(Common.DT_LAST_MODIFIED, LocalDateTime.class));
            entry.setUserIdLastModified(rs.getInt(Common.USER_ID_LAST_MODIFIED));
            if (listIdToListEntry.containsKey(entry.getListId())) {
                listIdToListEntry.get(entry.getListId()).add(entry);
            } else {
                List<ListEntry> list = new ArrayList<>();
                list.add(entry);
                listIdToListEntry.put(entry.getListId(), list);
            }
        });
        listIdToListEntry.values().stream().forEach(list -> listEntries.add(list));
        return listEntries;
    }

    public boolean createListEntry(ListEntry entry) {
        LOG.info("Inserting ListEntry Object {}", entry);
        String query = ListSql.CREATE_LIST_ENTRY;
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue(Common.URL, entry.getUrl());
        params.addValue(Common.TITLE, entry.getTitle());
        params.addValue(Common.DESCRIPTION, entry.getDescription());
        params.addValue(Common.PRIORITY, entry.getPriority());
        params.addValue(Common.LIST_ID, entry.getListId());
        params.addValue(Common.USER_ID_OWNER, entry.getUserIdOwner());
        params.addValue(Common.IS_PURCHASED, entry.getIsPurchased());
        params.addValue(Common.USER_ID_LAST_MODIFIED, entry.getUserIdOwner());
        return namedParameterJdbcTemplate.update(query, params) > 0;
    }

    public boolean updateListEntry(ListEntry entry) {
        LOG.info("Updating GroupMappingList Object {}", entry);
        String query = ListSql.UPDATE_LIST_ENTRY;
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue(Common.URL, entry.getUrl());
        params.addValue(Common.TITLE, entry.getTitle());
        params.addValue(Common.DESCRIPTION, entry.getDescription());
        params.addValue(Common.PRIORITY, entry.getPriority());
        params.addValue(Common.IS_PURCHASED, entry.getIsPurchased());
        params.addValue(Common.USER_ID_PURCHASED, entry.getUserIdPurchased());
        params.addValue(Common.IS_ACTIVE, entry.getIsActive());
        params.addValue(Common.USER_ID_LAST_MODIFIED, entry.getUserIdOwner());
        return namedParameterJdbcTemplate.update(query, params) > 0;
    }

    public boolean deactivateListEntry(Integer id, User user) {
        LOG.info("Updating GroupMappingUser Object to INACTIVE {}", id);
        String query = ListSql.UPDATE_LIST_ENTRY_INACTIVE;
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue(Common.IS_ACTIVE, false);
        params.addValue(Common.USER_ID_LAST_MODIFIED, user.getId());
        params.addValue(Common.ID, id);
        return namedParameterJdbcTemplate.update(query, params) > 0;
    }
}
