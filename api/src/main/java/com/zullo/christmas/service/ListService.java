package com.zullo.christmas.service;

import java.util.HashMap;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zullo.christmas.exception.ChristmasException;
import com.zullo.christmas.model.api.CreateListRequest;
import com.zullo.christmas.model.database.GroupMappingList;
import com.zullo.christmas.model.database.ListEntity;
import com.zullo.christmas.model.database.ListEntry;
import com.zullo.christmas.model.database.User;
import com.zullo.christmas.repository.GroupRepository;
import com.zullo.christmas.repository.ListRepository;

@Service
public class ListService {
    Logger LOG = LoggerFactory.getLogger(ListService.class);

    GroupRepository groupRepository;
    ListRepository listRepository;
    AuthenticationService authenticationService;

    @Autowired
    public ListService(GroupRepository groupRepository, ListRepository listRepository, AuthenticationService authenticationService) {
        this.groupRepository = groupRepository;
        this.listRepository = listRepository;
        this.authenticationService = authenticationService;
    }

    public int createList(CreateListRequest request, User requestor) {
        LOG.debug("Entered createList request={}", request);
        if (!authenticationService.canUserReadGroup(request.getGroupId(), requestor)){
            return -1;
        }
        int listId = listRepository.createListEntity(request.getList());
        if (listId == -1){
            throw new ChristmasException("An error occurred when inserting the list!");
        }
        GroupMappingList gml = new GroupMappingList();
        gml.setGroupId(request.getGroupId());
        gml.setListId(listId);
        gml.setUserIdLastModified(request.getList().getUserIdLastModified());
        groupRepository.createGroupMappingList(gml);

        return listId;
    }

    public int updateList(ListEntity request, User requestor) {
        LOG.trace("Entered updateList");
        if (!authenticationService.canUserDeleteList(request, requestor)){
            LOG.warn("User {} is not authorized to update list {}", requestor, request.getId());
            return -1;
        }
        return listRepository.updateListEntity(request);
    }

    public boolean deleteList(Integer listId, User user) {
        LOG.trace("Entered deleteList");
        if (!authenticationService.canUserDeleteList(listRepository.getListById(listId), user)){
            LOG.warn("User {} is not authorized to delete list {}", user, listId);
            return false;
        }
        listRepository.deactivateListEntity(listId, user);
        return true;
    }

    public List<ListEntity> getAllListsForUser(User user) {
        return listRepository.getAllActiveListsForOwner(user.getId());
    }

    public ListEntity getListById(Integer listId){
        return listRepository.getListById(listId);
    }

    public int createListEntry(ListEntry entry, User requestor){
        if (!authenticationService.canUserDeleteList(listRepository.getListById(entry.getListId()), requestor)){
            LOG.warn("User {} is not authorized to add entry to list {}", requestor, entry.getListId());
            return -1;
        }
        return listRepository.createListEntry(entry);
    }

    public boolean updateListEntry(ListEntry entry, User requestor){
        if (!authenticationService.canUserDeleteListEntry(entry, requestor) && authenticationService.canUserReadList(entry.getListId(), requestor)){
            // Allow toggling purchase status if user can read list
            ListEntry existingEntry = listRepository.getListEntryById(entry.getId());
            if (existingEntry.getIsPurchased() != entry.getIsPurchased()){
                existingEntry.setIsPurchased(entry.getIsPurchased());
                return listRepository.updateListEntry(existingEntry, requestor);
            }
        } else if (authenticationService.canUserDeleteListEntry(entry, requestor)){
            //Only owner or admin can update entry details
            return listRepository.updateListEntry(entry, requestor);
        }
        return false;
    }

    public boolean deactivateListEntry(Integer id, User user){
        if (!authenticationService.canUserDeleteListEntry(listRepository.getListEntryById(id), user)){
            LOG.warn("User {} is not authorized to delete list entry {}", user, id);
            return false;
        }
        return listRepository.deactivateListEntry(id, user);
    }

    public HashMap<Integer, List<ListEntry>> getAllActiveEntriesForList(List<Integer> listIds, User requestor){
        HashMap<Integer, List<ListEntry>> entries = listRepository.getAllActiveEntriesForList(listIds);
        HashMap<Integer, List<ListEntry>> authorizedEntries = new HashMap<>();
        for (Integer listId : entries.keySet()){
            if (authenticationService.canUserReadList(listId, requestor)){
                authorizedEntries.put(listId, entries.get(listId));
            }
        }
        return authorizedEntries;
    }

    public List<ListEntity> getAllActiveListsForGroupIds(List<Integer> groupIds, User requestor){
        List<ListEntity> lists = listRepository.getAllActiveListsForGroup(groupIds);
        List<ListEntity> authorizedLists = lists.stream()
                .filter(l -> authenticationService.canUserReadList(l.getId(), requestor))
                .toList();
        return authorizedLists;
    }

}
