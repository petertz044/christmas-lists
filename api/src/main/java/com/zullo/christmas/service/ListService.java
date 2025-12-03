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

    @Autowired
    public ListService(GroupRepository groupRepository, ListRepository listRepository) {
        this.groupRepository = groupRepository;
        this.listRepository = listRepository;
    }

    public int createList(CreateListRequest request) {
        LOG.debug("Entered createList request={}", request);
        int listId = listRepository.createListEntity(request.getList());
        //Need the list id
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

    public boolean updateList(ListEntity request) {
        LOG.trace("Entered updateList");
        listRepository.updateListEntity(request);

        return true;
    }

    public boolean deleteList(Integer id, User user) {
        LOG.trace("Entered deleteList");
        listRepository.deactivateListEntity(id, user);
        return true;
    }

    public List<ListEntity> getAllListsForUser(User user) {
        return listRepository.getAllActiveListsForOwner(user.getId());
    }

    public ListEntity getListById(Integer listId){
        return listRepository.getListFromId(listId);
    }

    public int createListEntry(ListEntry entry){
        return listRepository.createListEntry(entry);
    }

    public boolean updateListEntry(ListEntry entry, User requestor){
        return listRepository.updateListEntry(entry, requestor);
    }

    public boolean deactivateListEntry(Integer id, User user){
        return listRepository.deactivateListEntry(id, user);
    }

    public HashMap<Integer, List<ListEntry>> getAllActiveEntriesForList(List<Integer> ids){
        return listRepository.getAllActiveEntriesForList(ids);
    }

    public List<ListEntity> getAllActiveListsForGroupIds(List<Integer> ids){
        return listRepository.getAllActiveListsForGroup(ids);
    }

}
