package com.romantulchak.clouddisk.controller;

import com.romantulchak.clouddisk.payload.request.LoginRequest;
import com.romantulchak.clouddisk.payload.request.SignupRequest;
import com.romantulchak.clouddisk.payload.response.JwtResponse;
import com.romantulchak.clouddisk.service.AuthService;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@CrossOrigin(value = "*", maxAge = 3600L)
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService){
        this.authService = authService;
    }

    @PostMapping("/signin")
    public JwtResponse signin(@Valid @RequestBody LoginRequest loginRequest){
        return authService.authenticateUser(loginRequest);
    }

    @PostMapping("/registration")
    public void registration(@Valid @RequestBody SignupRequest signupRequest){
        authService.registerUser(signupRequest);
    }

}
