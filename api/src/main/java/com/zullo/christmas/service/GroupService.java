package com.zullo.christmas.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zullo.christmas.constants.ApplicationConstants;
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

    public List<Group> getGroups(User user) {
        if (user.getRole().equals(ApplicationConstants.ADMIN)) {
            return groupRepository.getAllGroups();
        } else {
            // TODO: Return groups user is owner or member of
            return null;
        }

    }

    public Group getGroupById(Integer id) {
        return groupRepository.getGroupById(id);
    }

    public int createGroup(Group request) {
        return groupRepository.createGroup(request);
    }

    public boolean updateGroup(Group request) {
        return groupRepository.updateGroup(request);
    }

    public boolean deactivateGroup(Integer id, User user) {
        return groupRepository.deactivateGroup(id, user);
    }

    public int createGroupMappingList(GroupMappingList request) {
        // TODO: Validate user is admin or member of group
        // TODO: Validate a mapping between these IDs does not already exist
        return groupRepository.createGroupMappingList(request);
    }

    public boolean updateGroupMappingList(GroupMappingList request) {
        // TODO: Validate user is admin or member of group
        // TODO: Validate existing object is not currently inactive
        return groupRepository.updateGroupMappingList(request);
    }

    public boolean deactivateGroupMappingList(Integer listId, Integer groupId, User user) {
        // TODO: Validate user is admin or member of group
        return groupRepository.deactivateGroupMappingList(listId, groupId, user);
    }

    public List<Group> getAllGroupsForList(Integer id) {
        // TODO: Validate user is admin or member of group
        return groupRepository.getAllActiveGroupsForList(id);
    }

    public int createGroupMappingUser(GroupMappingUser request) {
        // TODO: Validate user is admin or member of group
        // TODO: Validate a mapping between these IDs does not already exist
        return groupRepository.createGroupMappingUser(request);
    }

    public boolean updateGroupMappingUser(GroupMappingUser request) {
        // TODO: Validate user is admin or member of group
        // TODO: Validate existing object is not currently inactive
        return groupRepository.updateGroupMappingUser(request);
    }

    public boolean deactivateGroupMappingUser(Integer userId, Integer groupId, User user) {
        // TODO: Validate user is admin or member of group
        return groupRepository.deactivateGroupMappingUser(userId, groupId, user);
    }

    public List<Group> getAllGroupsForUser(Integer id) {
        return groupRepository.getAllActiveGroupsForUser(id);
    }

}
