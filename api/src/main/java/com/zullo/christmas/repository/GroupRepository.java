package com.zullo.christmas.repository;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import com.zullo.christmas.constants.sql.Common;
import com.zullo.christmas.constants.sql.GroupSQL;
import com.zullo.christmas.model.database.Group;
import com.zullo.christmas.model.database.GroupMappingList;
import com.zullo.christmas.model.database.GroupMappingUser;
import com.zullo.christmas.model.database.ListEntity;
import com.zullo.christmas.model.database.User;

@Repository
public class GroupRepository {
    Logger LOG = LoggerFactory.getLogger(GroupRepository.class);

    NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Autowired
    public GroupRepository(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    public boolean createGroupMappingList(GroupMappingList gml){
        LOG.info("Inserting GroupMappingList Object {}", gml);
        String query = GroupSQL.CREATE_GROUP_MAPPING_LIST;
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue(Common.GROUP_ID, gml.getGroupId());
        params.addValue(Common.LIST_ID, gml.getListId());
        params.addValue(Common.USER_ID_LAST_MODIFIED, gml.getUserIdLastModified());
        return namedParameterJdbcTemplate.update(query, params) > 0;
    }

    public boolean updateGroupMappingList(GroupMappingList gml){
        LOG.info("Updating GroupMappingList Object {}", gml);
        String query = GroupSQL.UPDATE_GROUP_MAPPING_LIST;
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue(Common.GROUP_ID, gml.getGroupId());
        params.addValue(Common.LIST_ID, gml.getListId());
        params.addValue(Common.IS_ACTIVE, gml.getIsActive());
        params.addValue(Common.USER_ID_LAST_MODIFIED, gml.getUserIdLastModified());
        params.addValue(Common.ID, gml.getId());
        return namedParameterJdbcTemplate.update(query, params) > 0;
    }

    public boolean deactivateGroupMappingList(Integer id, User user){
        LOG.info("Updating GroupMappingList Object to INACTIVE {}", id);
        String query = GroupSQL.UPDATE_GROUP_MAPPING_LIST_INACTIVE;
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue(Common.IS_ACTIVE, false);
        params.addValue(Common.USER_ID_LAST_MODIFIED, user.getId());
        params.addValue(Common.ID, id);
        return namedParameterJdbcTemplate.update(query, params) > 0;
    }

    public List<Group> getAllActiveGroupsForUser(Integer id) {
        LOG.info("Getting all active groups for user {}", id);
        String query = GroupSQL.SELECT_ACTIVE_GROUPS_FOR_USER_ID;
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue(Common.USER_ID, id);
        List<Group> groups = new ArrayList<>();
        namedParameterJdbcTemplate.query(query, params, rs -> {
            Group group = new Group();
            group.setId(rs.getInt(Common.ID));
            groups.add(group);
        });
        return groups;
    }

    public List<ListEntity> getAllActiveListsForGroup(List<Integer> ids) {
        LOG.info("Getting all active lists for groups {}", ids);
        String query = GroupSQL.SELECT_ACTIVE_LISTS_FOR_GROUP_IDS;
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue(Common.GROUP_ID, ids);
        List<ListEntity> lists = new ArrayList<>();
        namedParameterJdbcTemplate.query(query, params, rs -> {
            ListEntity list = new ListEntity();
            list.setId(rs.getInt(Common.LIST_ID));
            list.setTitle(rs.getString(Common.TITLE));
            list.setDescription(rs.getString(Common.DESCRIPTION));
            list.setUserIdOwner(rs.getInt(Common.USER_ID_OWNER));
            lists.add(list);
        });
        return lists;
    }

    public List<Group> getAllActiveGroupsForList(Integer id){
        LOG.info("Entering getAllActiveGroupsForList id={}", id);
        String query = GroupSQL.SELECT_ACTIVE_GROUPS_FOR_LIST_ID;
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue(Common.LIST_ID, id);
        List<Group> groups = new ArrayList<>();
        namedParameterJdbcTemplate.query(query, params, rs -> {
            Group group = new Group();
            group.setId(rs.getInt(Common.GROUP_ID));
            groups.add(group);
        });
        return groups;
    }


    public boolean createGroupMappingUser(GroupMappingUser gmu){
        LOG.info("Inserting GroupMappingUser Object {}", gmu);
        String query = GroupSQL.CREATE_GROUP_MAPPING_USER;
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue(Common.GROUP_ID, gmu.getGroupId());
        params.addValue(Common.USER_ID, gmu.getUserId());
        params.addValue(Common.USER_ID_LAST_MODIFIED, gmu.getUserIdLastModified());
        return namedParameterJdbcTemplate.update(query, params) > 0;
    }

    public boolean updateGroupMappingUser(GroupMappingUser gmu){
        LOG.info("Updating GroupMappingList Object {}", gmu);
        String query = GroupSQL.UPDATE_GROUP_MAPPING_USER;
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue(Common.GROUP_ID, gmu.getGroupId());
        params.addValue(Common.USER_ID, gmu.getUserId());
        params.addValue(Common.IS_ACTIVE, gmu.getIsActive());
        params.addValue(Common.USER_ID_LAST_MODIFIED, gmu.getUserIdLastModified());
        params.addValue(Common.ID, gmu.getId());
        return namedParameterJdbcTemplate.update(query, params) > 0;
    }

    public boolean deactivateGroupMappingUser(Integer id, User user){
        LOG.info("Updating GroupMappingUser Object to INACTIVE {}", id);
        String query = GroupSQL.UPDATE_GROUP_MAPPING_USER_INACTIVE;
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue(Common.IS_ACTIVE, false);
        params.addValue(Common.USER_ID_LAST_MODIFIED, user.getId());
        params.addValue(Common.ID, id);
        return namedParameterJdbcTemplate.update(query, params) > 0;
    }

}
