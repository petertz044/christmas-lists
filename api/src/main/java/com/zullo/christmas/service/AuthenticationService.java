package com.zullo.christmas.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.zullo.christmas.model.Api.LoginRequest;
import com.zullo.christmas.model.Api.LoginResponse;
import com.zullo.christmas.model.Api.RegisterRequest;
import com.zullo.christmas.model.Api.RegisterResponse;
import com.zullo.christmas.model.Api.TestRequest;
import com.zullo.christmas.model.Database.User;
import com.zullo.christmas.repository.UserRepository;

@Service
public class AuthenticationService {
    Logger LOG = LoggerFactory.getLogger(AuthenticationService.class);

    UserRepository userRepository;
    JwtService jwtService;
    BCryptPasswordEncoder passwordEncoder;

    @Autowired
    public AuthenticationService(UserRepository userRepository, 
                                 JwtService jwtService){
        this.userRepository = userRepository;
        this.jwtService = jwtService;
        this.passwordEncoder = new BCryptPasswordEncoder();
    }


    public LoginResponse attemptLogin(LoginRequest request){
        LoginResponse response = new LoginResponse();

        User user = userRepository.getUserByUsername(request.getUsername());


        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())){
            response.setMessage("Invalid username or password!");
            return response;
        }

        String jwt = jwtService.generateJwt(user);
        
        response.setJwt(jwt);
        response.setMessage("Success");

        return response;
    }
    
    public RegisterResponse saveUser(RegisterRequest request){
        RegisterResponse response = new RegisterResponse();

        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole('U');

        boolean success = userRepository.insertUser(user);

        response.setMessage(success ? "Success" : "Error");
        return response;
    }

    public String test(TestRequest request){
        userRepository.getUserByUsername(request.getUsername());

        return "Ttest";
    }

}
