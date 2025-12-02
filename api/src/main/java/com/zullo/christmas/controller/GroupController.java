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
    public GroupController(GroupService groupService, ListService listService, JwtService jwtService){
        this.groupService = groupService;
        this.listService = listService;
        this.jwtService = jwtService;
    }

    @PostMapping("/v1/group")
    public ResponseEntity<String> createGroup(){


        return new ResponseEntity<>(null, HttpStatus.OK);
    }

    @PostMapping("/v1/group/{groupId}")
    public ResponseEntity<String> updateGroup(@PathVariable Integer groupId){



        return new ResponseEntity<>(null, HttpStatus.OK);
    }

    @GetMapping("/v1/list/{listId}/mappings")
    public ResponseEntity<List<Group>> getGroupMappingLists(@RequestHeader(name = "Authorization") String jwt, @PathVariable Integer listId) {
        User user = jwtService.extractUserFromJwt(jwt);

        ListEntity list = listService.getListById(listId);
        if (!CommonUtils.isOwnerOrAdmin(user, list.getId())){
            return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
        }
        
        List<Group> groups = groupService.getAllGroupsForList(listId);

        if (CollectionUtils.isEmpty(groups)) {
            return new ResponseEntity<>(null, HttpStatus.OK);
        }

        return new ResponseEntity<>(groups, HttpStatus.OK);

    }

    @PostMapping("/v1/list/{listId}/mapping")
    public ResponseEntity<String> createGroupMappingList(@RequestHeader(name = "Authorization") String jwt,
                                                         @RequestBody GroupMappingList request,
                                                         @PathVariable Integer listId) {
        LOG.debug("Initiating createList request={}", request);
        User user = jwtService.extractUserFromJwt(jwt);

        ListEntity list = listService.getListById(listId);
        if (!CommonUtils.isOwnerOrAdmin(user, list.getId())){
            return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
        }
        
        groupService.createGroupMappingList(request);

        return new ResponseEntity<>(null, HttpStatus.OK);
    }

    @PostMapping("/v1/list/{listId}/mapping/{mappingId}")
    public ResponseEntity<String> updateGroupMappingList(@RequestBody GroupMappingList request, @PathVariable Integer mappingId) {
        LOG.debug("Initiating updateList id={} request={}", mappingId, request);
        if (request.getId() == null || request.getId() == mappingId){
            return new ResponseEntity<>("Request ID does not match URL ID", HttpStatus.BAD_REQUEST);
        }

        groupService.updateGroupMappingList(request);

        return new ResponseEntity<>(null, HttpStatus.OK);
    }

    @DeleteMapping("/v1/list/{listId}/mapping/{mappingId}")
    public ResponseEntity<String> deleteGroupMappingList(@RequestHeader(name = "Authorization") String jwt, 
                                                         @PathVariable Integer mappingId) {
        User user = jwtService.extractUserFromJwt(jwt);
        groupService.deactivateGroupMappingList(mappingId, user);

        return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
    }


    @GetMapping("/v1/user/{userId}/mappings")
    public ResponseEntity<List<Group>> getGroupMappingUsers(@RequestHeader(name = "Authorization") String jwt) {
        User user = jwtService.extractUserFromJwt(jwt);

        List<Group> groups = groupService.getAllGroupsForUser(user.getId());

        if (CollectionUtils.isEmpty(groups)) {
            return new ResponseEntity<>(null, HttpStatus.OK);
        }

        return new ResponseEntity<>(groups, HttpStatus.OK);

    }

    @PostMapping("/v1/user/{userId}/mapping")
    public ResponseEntity<String> createGroupMappingUser(@RequestBody GroupMappingUser request) {
        LOG.debug("Initiating createList request={}", request);

        groupService.createGroupMappingUser(request);

        return new ResponseEntity<>(null, HttpStatus.OK);
    }

    @PostMapping("/v1/user/{userId}/mapping/{mappingId}")
    public ResponseEntity<String> updateGroupMappingUser(@RequestBody GroupMappingUser request, @PathVariable Integer mappingId) {
        LOG.debug("Initiating updateList id={} request={}", mappingId, request);
        if (request.getId() == null || request.getId() == mappingId){
            return new ResponseEntity<>("Request ID does not match URL ID", HttpStatus.BAD_REQUEST);
        }

        groupService.updateGroupMappingUser(request);

        return new ResponseEntity<>(null, HttpStatus.OK);
    }

    @DeleteMapping("/v1/user/{userId}/mapping/{mappingId}")
    public ResponseEntity<String> deleteGroupMappingUser(@RequestHeader(name = "Authorization") String jwt, @PathVariable Integer mappingId) {
        LOG.debug("Initiating deleteGroupMappingUser id={}", mappingId);
        User user = jwtService.extractUserFromJwt(jwt);
        
        groupService.deactivateGroupMappingUser(mappingId, user);

        return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
    }



}
