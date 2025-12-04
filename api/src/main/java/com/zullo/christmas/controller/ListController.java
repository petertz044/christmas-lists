package com.zullo.christmas.controller;

import java.util.HashMap;
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

import com.zullo.christmas.model.api.CreateListRequest;
import com.zullo.christmas.model.database.ListEntity;
import com.zullo.christmas.model.database.ListEntry;
import com.zullo.christmas.model.database.User;
import com.zullo.christmas.service.JwtService;
import com.zullo.christmas.service.ListService;

@CrossOrigin()
@RestController()
@RequestMapping("/")
public class ListController {
    /**
     * TODO: Update all loggers
     */


    Logger LOG = LoggerFactory.getLogger(ListController.class);

    ListService listService;
    JwtService jwtService;

    @Autowired
    public ListController(ListService listService, JwtService jwtService) {
        this.listService = listService;
        this.jwtService = jwtService;
    }

    @GetMapping("/v1/lists")
    public ResponseEntity<List<ListEntity>> getLists(@RequestHeader(name = "Authorization") String jwt) {
        User user = jwtService.extractUserFromJwt(jwt);

        List<ListEntity> lists = listService.getAllListsForUser(user);

        return new ResponseEntity<>(lists, HttpStatus.OK);

    }

    @PostMapping("/v1/list")
    public ResponseEntity<Integer> createList(@RequestHeader(name = "Authorization") String jwt, @RequestBody CreateListRequest request) {
        LOG.debug("Initiating createList request={}", request);
        User user = jwtService.extractUserFromJwt(jwt);

        request.getList().setUserIdOwner(user.getId());
        request.getList().setUserIdLastModified(user.getId());
        Integer listId = listService.createList(request, user);
        if (listId == -1) {
            return new ResponseEntity<>(-1, HttpStatus.FORBIDDEN);
        }
        return new ResponseEntity<>(listId, HttpStatus.OK);
    }

    @PostMapping("/v1/list/{listId}")
    public ResponseEntity<String> updateList(@RequestHeader(name = "Authorization") String jwt, @RequestBody ListEntity request, @PathVariable Integer listId) {
        LOG.debug("Initiating updateList id={} request={}", listId, request);
        User user = jwtService.extractUserFromJwt(jwt);
        request.setUserIdLastModified(user.getId());
        request.setId(listId);

        int result = listService.updateList(request, user);

        if (result == -1) {
            return new ResponseEntity<>("User is not authorized to update the list", HttpStatus.FORBIDDEN);
        } else if (result == 0) {
            return new ResponseEntity<>("An error occurred when updating the list", HttpStatus.INTERNAL_SERVER_ERROR);
        } else if (result > 1) {
            LOG.error("------CRITICAL ERROR------ Multiple rows affected when updating list id {}: {}", listId, result);
            return new ResponseEntity<>("An error occurred when updating the list", HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<>(null, HttpStatus.OK);
    }

    @DeleteMapping("/v1/list/{listId}")
    public ResponseEntity<String> deleteList(@RequestHeader(name = "Authorization") String jwt, @PathVariable Integer listId) {
        LOG.debug("Initiating createList id={} request={}", listId);
        User user = jwtService.extractUserFromJwt(jwt);

        listService.deleteList(listId, user);

        return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
    }

    @GetMapping("/v1/list/{listId}/entries")
    public ResponseEntity<HashMap<Integer, List<ListEntry>>> getListEntries(@RequestHeader(name = "Authorization") String jwt, @PathVariable Integer listId) {
        User user = jwtService.extractUserFromJwt(jwt);

        HashMap<Integer, List<ListEntry>> entries = listService.getAllActiveEntriesForList(List.of(listId), user);

        return new ResponseEntity<>(entries, HttpStatus.OK);

    }

    @PostMapping("/v1/list/{listId}/entry")
    public ResponseEntity<Integer> createListEntry(@RequestHeader(name = "Authorization") String jwt, @RequestBody ListEntry request, @PathVariable Integer listId) {
        LOG.debug("Initiating createList request={}", request);
        User user = jwtService.extractUserFromJwt(jwt);
        request.setUserIdOwner(user.getId());
        request.setListId(listId);
        request.setUserIdLastModified(user.getId());

        Integer id = listService.createListEntry(request, user);
        if (id == -1){
            return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
        }
        return new ResponseEntity<>(id, HttpStatus.OK);
    }

    @PostMapping("/v1/list/{listId}/entry/{entryId}")
    public ResponseEntity<String> updateListEntry(@RequestHeader(name = "Authorization") String jwt, @RequestBody ListEntry request, @PathVariable Integer entryId,  @PathVariable Integer listId) {
        LOG.debug("Initiating updateList id={} request={}", entryId, request);
        User user = jwtService.extractUserFromJwt(jwt);
        request.setId(entryId);
        request.setListId(listId);
        request.setUserIdLastModified(user.getId());
        
        listService.updateListEntry(request, user);

        return new ResponseEntity<>(null, HttpStatus.OK);
    }

    @DeleteMapping("/v1/list/{listId}/entry/{entryId}")
    public ResponseEntity<String> deleteListEntry(@RequestHeader(name = "Authorization") String jwt, @PathVariable Integer entryId) {
        LOG.debug("Initiating createList id={}", entryId);
        User user = jwtService.extractUserFromJwt(jwt);

        listService.deactivateListEntry(entryId, user);

        return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
    }


}
