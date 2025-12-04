package com.zullo.christmas.repository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import com.zullo.christmas.constants.sql.CommonSql;
import com.zullo.christmas.constants.sql.GroupSql;
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

    public List<Group> getAllGroups(){
        LOG.info("Entering getGroups");
        String query = GroupSql.SELECT_ALL_GROUPS;
        MapSqlParameterSource params = new MapSqlParameterSource();
        List<Group> groups = new ArrayList<>();
        namedParameterJdbcTemplate.query(query, params, rs -> {
            Group group = new Group();
            group.setId(rs.getInt(CommonSql.ID));
            group.setTitle(rs.getString(CommonSql.TITLE));
            group.setDescription(rs.getString(CommonSql.DESCRIPTION));
            group.setUserIdOwner(rs.getInt(CommonSql.USER_ID_OWNER));
            group.setIsActive(rs.getBoolean(CommonSql.IS_ACTIVE));
            group.setDtCrtd(rs.getObject(CommonSql.DT_CRTD, LocalDateTime.class));
            group.setDtLastModified(rs.getObject(CommonSql.DT_LAST_MODIFIED, LocalDateTime.class));
            group.setUserIdLastModified(rs.getInt(CommonSql.USER_ID_LAST_MODIFIED));
            groups.add(group);
        });
        return groups;

    }

    public Group getGroupById(Integer id){
        LOG.debug("Entering getGroupById id={}", id);
        String query = GroupSql.SELECT_GROUP_BY_ID;
        MapSqlParameterSource params = new MapSqlParameterSource();
        Group group = new Group();
        namedParameterJdbcTemplate.query(query, params, rs -> {
            group.setId(rs.getInt(CommonSql.ID));
            group.setTitle(rs.getString(CommonSql.TITLE));
            group.setDescription(rs.getString(CommonSql.DESCRIPTION));
            group.setUserIdOwner(rs.getInt(CommonSql.USER_ID_OWNER));
            group.setIsActive(rs.getBoolean(CommonSql.IS_ACTIVE));
            group.setDtCrtd(rs.getObject(CommonSql.DT_CRTD, LocalDateTime.class));
            group.setDtLastModified(rs.getObject(CommonSql.DT_LAST_MODIFIED, LocalDateTime.class));
            group.setUserIdLastModified(rs.getInt(CommonSql.USER_ID_LAST_MODIFIED));
        });
        return group;
    }

    public int createGroup(Group group){
        LOG.info("Inserting createGroup Object {}", group);
        String query = GroupSql.CREATE_GROUP;
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue(CommonSql.TITLE, group.getTitle());
        params.addValue(CommonSql.DESCRIPTION, group.getDescription());
        params.addValue(CommonSql.USER_ID_OWNER, group.getUserIdOwner());
        params.addValue(CommonSql.USER_ID_LAST_MODIFIED, group.getUserIdOwner());
        KeyHolder id = new GeneratedKeyHolder();
        int inserted = namedParameterJdbcTemplate.update(query, params, id, new String[]{"id"});
        if (inserted == 1){
            return id.getKey().intValue();
        }
        return -1;
    }

    public int updateGroup(Group group){
        LOG.info("Updating Group Object {}", group);
        String query = GroupSql.UPDATE_GROUP;
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue(CommonSql.TITLE, group.getTitle());
        params.addValue(CommonSql.DESCRIPTION, group.getDescription());
        params.addValue(CommonSql.IS_ACTIVE, group.getIsActive());
        params.addValue(CommonSql.USER_ID_LAST_MODIFIED, group.getUserIdLastModified());
        params.addValue(CommonSql.ID, group.getId());
        LOG.debug("{} {}", query, params);
        return namedParameterJdbcTemplate.update(query, params);
    }

    public int deactivateGroup(Integer id, User user){
        LOG.info("Updating Group Object to INACTIVE {}", id);
        String query = GroupSql.UPDATE_GROUP_INACTIVE;
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue(CommonSql.IS_ACTIVE, false);
        params.addValue(CommonSql.USER_ID_LAST_MODIFIED, user.getId());
        params.addValue(CommonSql.ID, id);
        return namedParameterJdbcTemplate.update(query, params);
    }

    public int createGroupMappingList(GroupMappingList gml){
        LOG.info("Inserting GroupMappingList Object {}", gml);
        String query = GroupSql.CREATE_GROUP_MAPPING_LIST;
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue(CommonSql.GROUP_ID, gml.getGroupId());
        params.addValue(CommonSql.LIST_ID, gml.getListId());
        params.addValue(CommonSql.USER_ID_LAST_MODIFIED, gml.getUserIdLastModified());
        KeyHolder id = new GeneratedKeyHolder();
        int inserted = namedParameterJdbcTemplate.update(query, params, id, new String[]{"id"});
        if (inserted == 1){
            return id.getKey().intValue();
        }
        return -1;
    }

    public boolean updateGroupMappingList(GroupMappingList gml){
        LOG.info("Updating GroupMappingList Object {}", gml);
        String query = GroupSql.UPDATE_GROUP_MAPPING_LIST;
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue(CommonSql.GROUP_ID, gml.getGroupId());
        params.addValue(CommonSql.LIST_ID, gml.getListId());
        params.addValue(CommonSql.IS_ACTIVE, gml.getIsActive());
        params.addValue(CommonSql.USER_ID_LAST_MODIFIED, gml.getUserIdLastModified());
        params.addValue(CommonSql.ID, gml.getId());
        return namedParameterJdbcTemplate.update(query, params) > 0;
    }

    public boolean deactivateGroupMappingList(Integer listId, Integer groupId, User user){
        LOG.info("Updating GroupMappingList Object to INACTIVE list={}, group={}", listId, groupId);
        String query = GroupSql.UPDATE_GROUP_MAPPING_LIST_INACTIVE;
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue(CommonSql.IS_ACTIVE, false);
        params.addValue(CommonSql.USER_ID_LAST_MODIFIED, user.getId());
        params.addValue(CommonSql.LIST_ID, listId);
        params.addValue(CommonSql.GROUP_ID, groupId);
        return namedParameterJdbcTemplate.update(query, params) > 0;
    }

    public List<Group> getAllActiveGroupsForUser(Integer userId) {
        LOG.info("Getting all active groups for user {}", userId);
        String query = GroupSql.SELECT_ACTIVE_GROUPS_FOR_USER_ID;
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue(CommonSql.USER_ID, userId);
        List<Group> groups = new ArrayList<>();
        namedParameterJdbcTemplate.query(query, params, rs -> {
            Group group = new Group();
            group.setId(rs.getInt(CommonSql.GROUP_ID));
            group.setTitle(rs.getString(CommonSql.TITLE));
            group.setDescription(rs.getString(CommonSql.DESCRIPTION));
            group.setUserIdOwner(rs.getInt(CommonSql.USER_ID_OWNER));
            group.setIsActive(rs.getBoolean(CommonSql.IS_ACTIVE));
            group.setDtCrtd(rs.getObject(CommonSql.DT_CRTD, LocalDateTime.class));
            group.setDtLastModified(rs.getObject(CommonSql.DT_LAST_MODIFIED, LocalDateTime.class));
            group.setUserIdLastModified(rs.getInt(CommonSql.USER_ID_LAST_MODIFIED));
            groups.add(group);
        });
        return groups;
    }


    public List<Group> getAllActiveGroupsForList(Integer listId){
        LOG.info("Entering getAllActiveGroupsForList id={}", listId);
        String query = GroupSql.SELECT_ACTIVE_GROUPS_FOR_LIST_ID;
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue(CommonSql.LIST_ID, listId);
        List<Group> groups = new ArrayList<>();
        namedParameterJdbcTemplate.query(query, params, rs -> {
            Group group = new Group();
            group.setId(rs.getInt(CommonSql.GROUP_ID));
            group.setTitle(rs.getString(CommonSql.TITLE));
            group.setDescription(rs.getString(CommonSql.DESCRIPTION));
            group.setUserIdOwner(rs.getInt(CommonSql.USER_ID_OWNER));
            group.setIsActive(rs.getBoolean(CommonSql.IS_ACTIVE));
            group.setDtCrtd(rs.getObject(CommonSql.DT_CRTD, LocalDateTime.class));
            group.setDtLastModified(rs.getObject(CommonSql.DT_LAST_MODIFIED, LocalDateTime.class));
            group.setUserIdLastModified(rs.getInt(CommonSql.USER_ID_LAST_MODIFIED));
            groups.add(group);
        });
        return groups;
    }


    public int createGroupMappingUser(GroupMappingUser gmu){
        LOG.info("Inserting GroupMappingUser Object {}", gmu);
        String query = GroupSql.CREATE_GROUP_MAPPING_USER;
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue(CommonSql.GROUP_ID, gmu.getGroupId());
        params.addValue(CommonSql.USER_ID, gmu.getUserId());
        params.addValue(CommonSql.USER_ID_LAST_MODIFIED, gmu.getUserIdLastModified());
        KeyHolder id = new GeneratedKeyHolder();
        int inserted = namedParameterJdbcTemplate.update(query, params, id, new String[]{"id"});
        if (inserted == 1){
            return id.getKey().intValue();
        }
        return -1;
    }

    public boolean updateGroupMappingUser(GroupMappingUser gmu){
        LOG.info("Updating GroupMappingList Object {}", gmu);
        String query = GroupSql.UPDATE_GROUP_MAPPING_USER;
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue(CommonSql.GROUP_ID, gmu.getGroupId());
        params.addValue(CommonSql.USER_ID, gmu.getUserId());
        params.addValue(CommonSql.IS_ACTIVE, gmu.getIsActive());
        params.addValue(CommonSql.USER_ID_LAST_MODIFIED, gmu.getUserIdLastModified());
        params.addValue(CommonSql.ID, gmu.getId());
        return namedParameterJdbcTemplate.update(query, params) > 0;
    }

    public boolean deactivateGroupMappingUser(Integer userId, Integer groupId, User user){
        LOG.info("Updating GroupMappingUser Object to INACTIVE user={}, group={}", userId, groupId);
        String query = GroupSql.UPDATE_GROUP_MAPPING_USER_INACTIVE;
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue(CommonSql.IS_ACTIVE, false);
        params.addValue(CommonSql.USER_ID_LAST_MODIFIED, user.getId());
        params.addValue(CommonSql.USER_ID, userId);
        params.addValue(CommonSql.GROUP_ID, groupId);
        return namedParameterJdbcTemplate.update(query, params) > 0;
    }

    public List<User> getAllUsersInGroup(Integer groupId){
        LOG.info("Entering getAllUsersInGroup id={}", groupId);
        String query = GroupSql.SELECT_USERS_FROM_GROUP;
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue(CommonSql.GROUP_ID, groupId);
        List<User> users = new ArrayList<>();
        namedParameterJdbcTemplate.query(query, params, rs -> {
            User user = new User();
            user.setId(rs.getInt(CommonSql.ID));
            user.setUsername(rs.getString(CommonSql.USERNAME));
            users.add(user);
        });
        return users;
    }
}
