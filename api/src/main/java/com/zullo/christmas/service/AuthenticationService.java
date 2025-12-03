package com.zullo.christmas.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.zullo.christmas.model.api.LoginRequest;
import com.zullo.christmas.model.api.LoginResponse;
import com.zullo.christmas.model.api.RegisterRequest;
import com.zullo.christmas.model.api.RegisterResponse;
import com.zullo.christmas.model.api.TestRequest;
import com.zullo.christmas.model.database.Group;
import com.zullo.christmas.model.database.ListEntity;
import com.zullo.christmas.model.database.ListEntry;
import com.zullo.christmas.model.database.User;
import com.zullo.christmas.repository.UserRepository;

@Service
public class AuthenticationService {
    Logger LOG = LoggerFactory.getLogger(AuthenticationService.class);

    UserRepository userRepository;
    GroupService groupService;
    JwtService jwtService;
    BCryptPasswordEncoder passwordEncoder;

    @Autowired
    public AuthenticationService(UserRepository userRepository,
            JwtService jwtService,
            GroupService groupService) {
        this.userRepository = userRepository;
        this.jwtService = jwtService;
        this.groupService = groupService;
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

    public boolean updateUserRole(Integer userIdTarget, Integer userIdRequestor, String role) {
        LOG.info("Updating role for user: {}", userIdTarget);
        User requestUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        return userRepository.updateUserRole(userIdTarget, requestUser.getId(), role);
    }

    public String test(TestRequest request) {
        userRepository.getUserByUsername(request.getUsername());

        return "Test";
    }
/* 
    public boolean canUserAccessList(User user, ListEntity list) {
        return isOwnerOrAdmin(user, list.getUserIdOwner()) || canUserAccessGroup(user, group);
    }

    public  boolean canUserAccessGroup(User user, Group group, ){
        return isOwnerOrAdmin(user, group.getUserIdOwner()) || 
    }

    public boolean canUserAccessListEntry(User user, ListEntry listEntry) {

    }
*/

}
