package com.zullo.christmas.service;

import java.util.List;
import java.util.Optional;

import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zullo.christmas.constants.ApplicationConstants;
import com.zullo.christmas.model.database.Group;
import com.zullo.christmas.model.database.GroupMappingList;
import com.zullo.christmas.model.database.GroupMappingUser;
import com.zullo.christmas.model.database.ListEntity;
import com.zullo.christmas.model.database.User;
import com.zullo.christmas.repository.GroupRepository;
import com.zullo.christmas.repository.ListRepository;

@Service
public class GroupService {
    Logger LOG = LoggerFactory.getLogger(GroupService.class);

    GroupRepository groupRepository;
    AuthenticationService authenticationService;
    ListRepository listRepository;

    @Autowired
    public GroupService(GroupRepository groupRepository, AuthenticationService authenticationService, ListRepository listRepository) {
        this.groupRepository = groupRepository;
        this.authenticationService = authenticationService;
        this.listRepository = listRepository;
    }

    public List<Group> getGroups(User user) {
        if (user.getRole().equals(ApplicationConstants.ADMIN)) {
            return groupRepository.getAllGroups();
        } else {
            return groupRepository.getAllActiveGroupsForUser(user.getId());
        }

    }

    public Group getGroupById(Integer id) {
        return groupRepository.getGroupById(id);
    }

    public int createGroup(Group request) {
        return groupRepository.createGroup(request);
    }

    public int updateGroup(Group request, User requestor) {
        if (!authenticationService.canUserDeleteGroup(request, requestor)){
            LOG.warn("User {} is not authorized to update group {}", requestor, request.getId());
            return -1;
        }
        return groupRepository.updateGroup(request);
    }

    public int deactivateGroup(Integer id, User user) {
        if (!authenticationService.canUserDeleteGroup(getGroupById(id), user)){
            LOG.warn("User {} is not authorized to deactivate group {}", user, id);
            return -1;
        }
        return groupRepository.deactivateGroup(id, user);
    }

    public int createGroupMappingList(GroupMappingList request, User user) {
         ListEntity list = listRepository.getListById(request.getListId());
        if (!authenticationService.canUserDeleteList(list, user)){
            LOG.warn("User {} is not authorized to modify group {}", user, request.getGroupId());
            return -1;
        }
        if (Optional.ofNullable(groupRepository.getAllActiveGroupsForList(request.getListId())).orElse(List.of())
                .stream().map(Group::getId).anyMatch(i -> i == request.getGroupId())){
            LOG.warn("Mapping between list {} and group {} already exists!", request.getListId(), request.getGroupId());
            return -2;
        }
        return groupRepository.createGroupMappingList(request);
    }
/*
    public boolean updateGroupMappingList(GroupMappingList request) {
        // Validate user is admin or member of group
        // Validate existing object is not currently inactive
        return groupRepository.updateGroupMappingList(request);
    }
*/
    public boolean deactivateGroupMappingList(Integer listId, Integer groupId, User user) {
        ListEntity list = listRepository.getListById(listId);
        if (!authenticationService.canUserDeleteList(list, user)){
            LOG.warn("User {} is not authorized to modify list {}", user, groupId);
            return false;
        }
        return groupRepository.deactivateGroupMappingList(listId, groupId, user);
    }

    public List<Group> getAllGroupsForList(Integer listId, User requestor) {
        if (!authenticationService.canUserReadGroup(listId, requestor)){
            LOG.warn("User {} is not authorized to view groups for list {}", requestor, listId);
            return List.of();
        }
        List<Group> groups = groupRepository.getAllActiveGroupsForList(listId);
        List<Group> authorizedGroups = groups.stream()
                .filter(g -> authenticationService.canUserReadGroup(g.getId(), requestor))
                .toList();

        return authorizedGroups;
    }

    public int createGroupMappingUser(GroupMappingUser request, User user) {
        Group group = groupRepository.getGroupById(request.getGroupId());
        if (!authenticationService.canUserDeleteGroup(group, user)) {
            LOG.warn("User {} is not authorized to modify group with users {}", user, request.getGroupId());
            return -1;
        }
        if (Optional.ofNullable(groupRepository.getAllActiveGroupsForUser(request.getUserId())).orElse(List.of())
                .stream().map(Group::getId).anyMatch(i -> i == request.getGroupId())){
            LOG.warn("Mapping between user {} and group {} already exists!", request.getUserId(), request.getGroupId());
            return -2;
        }
        return groupRepository.createGroupMappingUser(request);
    }
/*
    public boolean updateGroupMappingUser(GroupMappingUser request) {
        // Validate user is admin or member of group
        //  Validate existing object is not currently inactive
        return groupRepository.updateGroupMappingUser(request);
    }
*/
    public boolean deactivateGroupMappingUser(Integer userId, Integer groupId, User user) {
        Group group = groupRepository.getGroupById(groupId);
        if (!authenticationService.canUserDeleteGroup(group, user)){
            LOG.warn("User {} is not authorized to modify group {}", user, groupId);
            return false;
        }
        return groupRepository.deactivateGroupMappingUser(userId, groupId, user);
    }

    public List<Group> getAllGroupsForUser(Integer id) {
        return groupRepository.getAllActiveGroupsForUser(id);
    }

    public List<User> getAllUsersForGroup(Integer id, User requestor) {
        if (!authenticationService.canUserReadGroup(id, requestor)) {
            LOG.warn("User {} is not authorized to view users for group {}", requestor, id);
            return List.of();
        }
        return groupRepository.getAllUsersInGroup(id);

    }
}
