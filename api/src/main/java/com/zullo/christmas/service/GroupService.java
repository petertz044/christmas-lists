package com.zullo.christmas.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zullo.christmas.model.database.Group;
import com.zullo.christmas.model.database.GroupMappingList;
import com.zullo.christmas.model.database.GroupMappingUser;
import com.zullo.christmas.model.database.User;
import com.zullo.christmas.repository.GroupRepository;

@Service
public class GroupService {
    Logger LOG = LoggerFactory.getLogger(GroupService.class);

    GroupRepository groupRepository;

    @Autowired
    public GroupService(GroupRepository groupRepository) {
        this.groupRepository = groupRepository;
    }

    public boolean createGroupMappingList(GroupMappingList request) {
        // TODO: Validate user is admin or member of group
        // TODO: Validate a mapping between these IDs does not already exist
        return groupRepository.createGroupMappingList(request);
    }

    public boolean updateGroupMappingList(GroupMappingList request) {
        // TODO: Validate user is admin or member of group
        // TODO: Validate existing object is not currently inactive
        return groupRepository.updateGroupMappingList(request);
    }

    public boolean deactivateGroupMappingList(Integer id, User user) {
        // TODO: Validate user is admin or member of group
        return groupRepository.deactivateGroupMappingList(id, user);
    }

    public List<Group> getAllGroupsForList(Integer id) {
        // TODO: Validate user is admin or member of group
        return groupRepository.getAllActiveGroupsForList(id);
    }

        public boolean createGroupMappingUser(GroupMappingUser request) {
        // TODO: Validate user is admin or member of group
        // TODO: Validate a mapping between these IDs does not already exist
        return groupRepository.createGroupMappingUser(request);
    }

    public boolean updateGroupMappingUser(GroupMappingUser request) {
        // TODO: Validate user is admin or member of group
        // TODO: Validate existing object is not currently inactive
        return groupRepository.updateGroupMappingUser(request);
    }

    public boolean deactivateGroupMappingUser(Integer id, User user) {
        // TODO: Validate user is admin or member of group
        return groupRepository.deactivateGroupMappingUser(id, user);
    }

    public List<Group> getAllGroupsForUser(Integer id) {
        return groupRepository.getAllActiveGroupsForUser(id);
    }

}
