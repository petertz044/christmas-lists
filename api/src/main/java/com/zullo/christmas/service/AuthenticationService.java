package com.zullo.christmas.service;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.zullo.christmas.constants.ApplicationConstants;
import com.zullo.christmas.model.api.LoginRequest;
import com.zullo.christmas.model.api.LoginResponse;
import com.zullo.christmas.model.api.RegisterRequest;
import com.zullo.christmas.model.api.RegisterResponse;
import com.zullo.christmas.model.api.TestRequest;
import com.zullo.christmas.model.database.Group;
import com.zullo.christmas.model.database.GroupMappingList;
import com.zullo.christmas.model.database.ListEntity;
import com.zullo.christmas.model.database.ListEntry;
import com.zullo.christmas.model.database.User;
import com.zullo.christmas.repository.GroupRepository;
import com.zullo.christmas.repository.ListRepository;
import com.zullo.christmas.repository.UserRepository;

@Service
public class AuthenticationService {
    Logger LOG = LoggerFactory.getLogger(AuthenticationService.class);

    UserRepository userRepository;
    GroupRepository groupRepository;
    ListRepository listRepository;
    JwtService jwtService;
    BCryptPasswordEncoder passwordEncoder;

    @Autowired
    public AuthenticationService(UserRepository userRepository,
            JwtService jwtService,
            GroupRepository groupRepository,
            ListRepository listRepository) {
        this.userRepository = userRepository;
        this.jwtService = jwtService;
        this.groupRepository = groupRepository;
        this.listRepository = listRepository;
        this.passwordEncoder = new BCryptPasswordEncoder();
    }

    public LoginResponse attemptLogin(LoginRequest request) {
        LOG.debug("Attempting login for user: {}", request.getUsername());
        LoginResponse response = new LoginResponse();

        User user = userRepository.getUserByUsername(request.getUsername());

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            response.setMessage("Invalid username or password!");
            return response;
        }

        String jwt = jwtService.generateJwt(user);
        userRepository.updateUserAfterLogin(user);

        response.setJwt(jwt);
        response.setMessage("Success");
        return response;
    }

    public RegisterResponse saveUser(RegisterRequest request) {
        LOG.info("Registering user: {}", request.getUsername());
        RegisterResponse response = new RegisterResponse();

        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole("U");

        boolean success = userRepository.insertUser(user);

        response.setMessage(success ? "Success" : "Error");
        return response;
    }

    public int updateUserRole(Integer userIdTarget, User requestor, String role) {
        LOG.info("Updating role for user: {}", userIdTarget);
        //User requestUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (!isUserAdmin(requestor)){
            return -1;
        }
        if (!List.of(ApplicationConstants.ADMIN, ApplicationConstants.USER).contains(role)){
            return -2;
        }
        
        return userRepository.updateUserRole(userIdTarget, requestor.getId(), role);
    }

    public String test(TestRequest request) {
        userRepository.getUserByUsername(request.getUsername());

        return "Test";
    }

    public boolean canUserReadGroup(Integer groupId, User user) {
        if (user.getRole().equals(ApplicationConstants.ADMIN)) {
            return true;
        }
        List<Group> userGroups = groupRepository.getAllActiveGroupsForUser(user.getId());
        if (CollectionUtils.isEmpty(userGroups)) {
            LOG.info("User {} is not a member of any group!", user);
            return false;
        }

        if (!userGroups.stream().map(Group::getId).anyMatch(i -> i == groupId)) {
            LOG.info("User {} is not a member of requested group!", user);
            return false;
        }
        return true;
    }

    /**
     * User must be one of the following
     * owner of list or
     * in the same group as the list or...
     * 
     * @param list
     * @param user
     * @return If the user can read list
     */
    public boolean canUserReadList(Integer listId, User user) {
        if (user.getRole().equals(ApplicationConstants.ADMIN)) {
            return true;
        }
        List<ListEntity> lists = Optional.ofNullable(listRepository.getAllActiveListsForOwner(user.getId())).orElse(List.of());
        List<Group> userGroups = Optional.ofNullable(groupRepository.getAllActiveGroupsForUser(user.getId())).orElse(List.of());
        List<Group> listGroups = Optional.ofNullable(groupRepository.getAllActiveGroupsForList(listId)).orElse(List.of());
        Set<Integer> listIds = lists.stream().map(ListEntity::getId).collect(Collectors.toSet());
        Set<Integer> listGroupIds = listGroups.stream().map(Group::getId).collect(Collectors.toSet());
        Set<Integer> userGroupIds = userGroups.stream().map(Group::getId).collect(Collectors.toSet());

        return listIds.contains(listId) ||
                listGroupIds.stream().anyMatch(userGroupIds::contains);
    }

    public boolean canUserDeleteList(ListEntity list, User user) {
        if (isUserAdmin(user)) {
            return true;
        }
        return list.getUserIdOwner() == user.getId();
    }

    public boolean canUserDeleteGroup(Group group, User user) {
        if (isUserAdmin(user)) {
            return true;
        }
        return group.getUserIdOwner() == user.getId();
    }

    public boolean canUserDeleteListEntry(ListEntry entry, User user) {
        if (isUserAdmin(user)) {
            return true;
        }
        return entry.getUserIdOwner() == user.getId();
    }

    public boolean isUserAdmin(User user) {
        return user.getRole().equals(ApplicationConstants.ADMIN);
    }
}
