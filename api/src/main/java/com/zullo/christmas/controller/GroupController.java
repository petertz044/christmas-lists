package com.zullo.christmas.controller;

import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.zullo.christmas.constants.ApplicationConstants;
import com.zullo.christmas.model.database.Group;
import com.zullo.christmas.model.database.GroupMappingList;
import com.zullo.christmas.model.database.GroupMappingUser;
import com.zullo.christmas.model.database.ListEntity;
import com.zullo.christmas.model.database.User;
import com.zullo.christmas.service.GroupService;
import com.zullo.christmas.service.JwtService;
import com.zullo.christmas.service.ListService;
import com.zullo.christmas.util.CommonUtils;

@CrossOrigin()
@RestController()
@RequestMapping("/")
public class GroupController {

    Logger LOG = LoggerFactory.getLogger(GroupController.class);

    GroupService groupService;
    ListService listService;
    JwtService jwtService;

    @Autowired
    public GroupController(GroupService groupService, ListService listService, JwtService jwtService) {
        this.groupService = groupService;
        this.listService = listService;
        this.jwtService = jwtService;
    }

    @GetMapping("/v1/groups")
    public ResponseEntity<List<Group>> getAllGroups(@RequestHeader(name = "Authorization") String jwt) {
        User user = jwtService.extractUserFromJwt(jwt);

        List<Group> groups = groupService.getGroups(user);

        return new ResponseEntity<>(groups, HttpStatus.OK);
    }

    @GetMapping("/v1/group/{groupId}/users")
    public ResponseEntity<List<User>> getAllUsersInGroup(@RequestHeader(name = "Authorization") String jwt,
            @PathVariable Integer groupId) {
        User user = jwtService.extractUserFromJwt(jwt);
    
        List<User> users = groupService.getAllUsersForGroup(groupId, user);

        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    @PostMapping("/v1/group")
    public ResponseEntity<Integer> createGroup(@RequestHeader(name = "Authorization") String jwt,
            @RequestBody Group request) {
        User user = jwtService.extractUserFromJwt(jwt);
        request.setUserIdOwner(user.getId());
        Integer id = groupService.createGroup(request);

        return new ResponseEntity<>(id, HttpStatus.OK);
    }

    @PostMapping("/v1/group/{groupId}")
    public ResponseEntity<String> updateGroup(@RequestHeader(name = "Authorization") String jwt,
            @RequestBody Group request,
            @PathVariable Integer groupId) {
        User user = jwtService.extractUserFromJwt(jwt);
        request.setId(groupId);
        request.setUserIdLastModified(user.getId());

        int result = groupService.updateGroup(request, user);
        if (result > 0){
            return new ResponseEntity<>(null, HttpStatus.OK);
        } else if (result == -1){
            return new ResponseEntity<>("User is not authorized to edit group", HttpStatus.UNAUTHORIZED);
        } else if (result == 0){
            return new ResponseEntity<>("No group was modified", HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>("Error occurred?", HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @DeleteMapping("/v1/group/{groupId}")
    public ResponseEntity<String> deleteGroup(@RequestHeader(name = "Authorization") String jwt,
            @PathVariable Integer groupId) {
        User user = jwtService.extractUserFromJwt(jwt);

        int result = groupService.deactivateGroup(groupId, user);

        if (result > 0){
            return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
        } else if (result == -1){
            return new ResponseEntity<>("User is not authorized to delete group", HttpStatus.UNAUTHORIZED);
        } else if (result == 0){
            return new ResponseEntity<>("No group was modified", HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>("Error occurred?", HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @GetMapping("/v1/list/{listId}/groups")
    public ResponseEntity<List<Group>> getGroupMappingLists(@RequestHeader(name = "Authorization") String jwt,
            @PathVariable Integer listId) {
        User user = jwtService.extractUserFromJwt(jwt);

        List<Group> groups = groupService.getAllGroupsForList(listId, user);

        return new ResponseEntity<>(groups, HttpStatus.OK);

    }

    @GetMapping("/v1/group/{groupId}/lists")
    public ResponseEntity<List<ListEntity>> getListsForGroup(@RequestHeader(name = "Authorization") String jwt,
            @PathVariable Integer groupId) {
        User user = jwtService.extractUserFromJwt(jwt);

        List<ListEntity> lists = listService.getAllActiveListsForGroupIds(List.of(groupId), user);

        return new ResponseEntity<>(lists, HttpStatus.OK);
    }

    @PostMapping("/v1/list/{listId}/group")
    public ResponseEntity<Integer> createGroupMappingList(@RequestHeader(name = "Authorization") String jwt,
            @RequestBody GroupMappingList request,
            @PathVariable Integer listId) {
        LOG.debug("Initiating createList request={}", request);
        User user = jwtService.extractUserFromJwt(jwt);
        request.setListId(listId);
        request.setUserIdLastModified(user.getId());

        Integer id = groupService.createGroupMappingList(request, user);
        if (id == -1){
            return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
        } else if (id == -2){
            return new ResponseEntity<>(null, HttpStatus.CONFLICT);
        }
        return new ResponseEntity<>(id, HttpStatus.OK);
    }

    /*
     * @PostMapping("/v1/list/{listId}/group/{mappingId}")
     * public ResponseEntity<String> updateGroupMappingList(@RequestBody
     * GroupMappingList request,
     * 
     * @PathVariable Integer mappingId) {
     * LOG.debug("Initiating updateList id={} request={}", mappingId, request);
     * if (request.getId() == null || request.getId() == mappingId) {
     * return new ResponseEntity<>("Request ID does not match URL ID",
     * HttpStatus.BAD_REQUEST);
     * }
     * 
     * groupService.updateGroupMappingList(request);
     * 
     * return new ResponseEntity<>(null, HttpStatus.OK);
     * }
     */
    
    @DeleteMapping("/v1/list/{listId}/group/{groupId}")
    public ResponseEntity<String> deleteGroupMappingList(@RequestHeader(name = "Authorization") String jwt,
            @PathVariable Integer listId,
            @PathVariable Integer groupId) {
        User user = jwtService.extractUserFromJwt(jwt);
        groupService.deactivateGroupMappingList(listId, groupId, user);

        return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
    }

    @GetMapping("/v1/user/{userId}/groups")
    public ResponseEntity<List<Group>> getGroupMappingUsers(@RequestHeader(name = "Authorization") String jwt) {
        User user = jwtService.extractUserFromJwt(jwt);

        List<Group> groups = groupService.getAllGroupsForUser(user.getId());

        return new ResponseEntity<>(groups, HttpStatus.OK);

    }

    @PostMapping("/v1/user/{userId}/group")
    public ResponseEntity<Integer> createGroupMappingUser(@RequestHeader(name = "Authorization") String jwt,
            @RequestBody GroupMappingUser request) {
        LOG.debug("Initiating createGroupMappingUser request={}", request);
        User user = jwtService.extractUserFromJwt(jwt);
        request.setUserIdLastModified(user.getId());

        Integer id = groupService.createGroupMappingUser(request, user);
        if (id == -1){
            return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
        } else if (id == -2){
            return new ResponseEntity<>(null, HttpStatus.CONFLICT);
        }

        return new ResponseEntity<>(id, HttpStatus.OK);
    }

    /*
     * @PostMapping("/v1/user/{userId}/group/{mappingId}")
     * public ResponseEntity<String> updateGroupMappingUser(@RequestHeader(name =
     * "Authorization") String jwt,
     * 
     * @RequestBody GroupMappingUser request,
     * 
     * @PathVariable Integer userId,
     * 
     * @PathVariable Integer mappingId) {
     * LOG.debug("Initiating updateList id={} request={}", mappingId, request);
     * User user = jwtService.extractUserFromJwt(jwt);
     * request.setUserIdLastModified(user.getId());
     * groupService.updateGroupMappingUser(request);
     * 
     * return new ResponseEntity<>(null, HttpStatus.OK);
     * }
     */

    @DeleteMapping("/v1/user/{userId}/group/{groupId}")
    public ResponseEntity<String> deleteGroupMappingUser(@RequestHeader(name = "Authorization") String jwt,
            @PathVariable Integer userId,
            @PathVariable Integer groupId) {
        LOG.debug("Initiating deleteGroupMappingUser id={}", groupId);
        User user = jwtService.extractUserFromJwt(jwt);

        groupService.deactivateGroupMappingUser(userId, groupId, user);

        return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
    }

}
