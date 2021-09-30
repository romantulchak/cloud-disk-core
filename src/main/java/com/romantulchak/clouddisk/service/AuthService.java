package com.romantulchak.clouddisk.service;

import com.romantulchak.clouddisk.payload.request.LoginRequest;
import com.romantulchak.clouddisk.payload.request.SignupRequest;
import com.romantulchak.clouddisk.payload.response.JwtResponse;

public interface AuthService {

    JwtResponse authenticateUser(LoginRequest loginRequest);

    void registerUser(SignupRequest signupRequest);
}
