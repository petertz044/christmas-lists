package com.zullo.christmas.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.zullo.christmas.model.Api.LoginRequest;
import com.zullo.christmas.model.Api.LoginResponse;
import com.zullo.christmas.model.Api.RegisterRequest;
import com.zullo.christmas.model.Api.RegisterResponse;
import com.zullo.christmas.model.Api.TestRequest;
import com.zullo.christmas.service.AuthenticationService;

@RestController()
@RequestMapping("/auth")
public class AuthenticationController {
    Logger LOG = LoggerFactory.getLogger(AuthenticationController.class);

    AuthenticationService authService;

    @Autowired
    public AuthenticationController(AuthenticationService authenticationService){
        this.authService = authenticationService;
    }


    @PostMapping("/login")
    public ResponseEntity<LoginResponse> loginUser(@RequestBody LoginRequest request){

        LoginResponse response = authService.attemptLogin(request);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/register")
    public ResponseEntity<RegisterResponse> registerUser(@RequestBody RegisterRequest request){

        RegisterResponse response = authService.saveUser(request);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/protected")
    public String testAuthProtection(){
        return "Success!";
    }

    @PostMapping("/testing")
    public String testing(@RequestBody TestRequest request){
        return authService.test(request);
    }

}
