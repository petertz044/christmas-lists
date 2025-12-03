package com.zullo.christmas.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.zullo.christmas.constants.ApplicationConstants;
import com.zullo.christmas.model.api.LoginRequest;
import com.zullo.christmas.model.api.LoginResponse;
import com.zullo.christmas.model.api.RegisterRequest;
import com.zullo.christmas.model.api.RegisterResponse;
import com.zullo.christmas.model.api.RoleChangeRequest;
import com.zullo.christmas.model.api.TestRequest;
import com.zullo.christmas.model.database.User;
import com.zullo.christmas.service.AuthenticationService;
import com.zullo.christmas.service.JwtService;

@CrossOrigin()
@RestController()
@RequestMapping("/auth")
public class AuthenticationController {
    Logger LOG = LoggerFactory.getLogger(AuthenticationController.class);

    AuthenticationService authService;
    JwtService jwtService;

    @Autowired
    public AuthenticationController(AuthenticationService authenticationService, JwtService jwtService) {
        this.authService = authenticationService;
        this.jwtService = jwtService;
    }

    @PostMapping("/v1/login")
    public ResponseEntity<LoginResponse> loginUser(@RequestBody LoginRequest request) {

        LoginResponse response = authService.attemptLogin(request);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/v1/register")
    public ResponseEntity<RegisterResponse> registerUser(@RequestBody RegisterRequest request) {

        RegisterResponse response = authService.saveUser(request);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/v1/role/{userId}")
    public ResponseEntity<String> changeUserRole(@RequestBody RoleChangeRequest request, 
                                                 @PathVariable Integer userId, 
                                                 @RequestHeader(name = "Authorization") String jwt){
        User requestor = jwtService.extractUserFromJwt(jwt);
        if (!requestor.getRole().equals("A")){
            return new ResponseEntity<>("User is not an Admin and cannot change the role of a user", HttpStatus.UNAUTHORIZED);
        }
        if (!List.of(ApplicationConstants.ADMIN, ApplicationConstants.USER).contains(request.role())){
            return new ResponseEntity<>("Requested role is invalid", HttpStatus.BAD_REQUEST);
        }
        
        authService.updateUserRole(userId, requestor.getId(), request.role());

        return new ResponseEntity<>(null, HttpStatus.OK);

    }


    @GetMapping("/v1/protected")
    public String testAuthProtection() {
        return "Success!";
    }

    @PostMapping("/v1/testing")
    public String testing(@RequestBody TestRequest request) {
        return authService.test(request);
    }

}
