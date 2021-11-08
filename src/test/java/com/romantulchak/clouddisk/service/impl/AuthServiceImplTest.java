package com.romantulchak.clouddisk.service.impl;

import com.romantulchak.clouddisk.exception.UserEmailAlreadyExistsException;
import com.romantulchak.clouddisk.exception.UsernameAlreadyExistsException;
import com.romantulchak.clouddisk.model.Role;
import com.romantulchak.clouddisk.model.enums.RoleType;
import com.romantulchak.clouddisk.payload.request.LoginRequest;
import com.romantulchak.clouddisk.payload.request.SignupRequest;
import com.romantulchak.clouddisk.payload.response.JwtResponse;
import com.romantulchak.clouddisk.repository.RoleRepository;
import com.romantulchak.clouddisk.repository.UserRepository;
import com.romantulchak.clouddisk.security.jwt.JwtUtils;
import com.romantulchak.clouddisk.service.DriveService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
class AuthServiceImplTest {

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private UserRepository userRepository;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private PasswordEncoder encoder;

    @Mock
    private JwtUtils jwtUtils;

    @Mock
    private DriveService driveService;

    @Mock
    private Authentication authentication;

    @InjectMocks
    private AuthServiceImpl authService;

    private String testJwt = "jwt";

    @Test
    void authenticateUser() {
        LoginRequest loginRequest = new LoginRequest("Test", "test");
        UsernamePasswordAuthenticationToken userAuthentication = new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword());
        UserDetailsImpl userDetails = new UserDetailsImpl(1L, "Test", "Test", "test", "test@gmail.com", "test", new ArrayList<>());
        Mockito.when(authentication.getPrincipal()).thenReturn(userDetails);
        Mockito.when(authenticationManager.authenticate(userAuthentication))
                .thenReturn(authentication);
        Mockito.when(jwtUtils.generateJwtToken(authentication)).thenReturn(testJwt);
        JwtResponse jwtResponse = authService.authenticateUser(loginRequest);
        assertNotNull(jwtResponse);
        assertEquals(testJwt, jwtResponse.getAccessToken());
        assertEquals(userDetails.getAuthorities().size(), jwtResponse.getRoles().size());
        assertEquals(userDetails.getId(), jwtResponse.getId());
        assertEquals(userDetails.getEmail(), jwtResponse.getEmail());
    }

    @Test
    void registerUser() {
        SignupRequest signupRequest = new SignupRequest("test", "Test", "Test", "test@gmail.com", "test");
        String encodedPassword = "encodedPassword";
        Role role = new Role(RoleType.ROLE_USER);
        Mockito.when(encoder.encode(signupRequest.getPassword())).thenReturn(encodedPassword);
        Mockito.when(roleRepository.findByName(RoleType.ROLE_USER)).thenReturn(Optional.of(role));
        authService.registerUser(signupRequest);
        Mockito.verify(userRepository, Mockito.times(1)).save(any());
    }

    @Test
    void registerUserThrowUsernameAlreadyExistsException() {
        SignupRequest signupRequest = new SignupRequest();
        signupRequest.setUsername("test");
        Mockito.when(userRepository.existsByUsername(signupRequest.getUsername())).thenReturn(true);
        assertThrows(UsernameAlreadyExistsException.class, () -> authService.registerUser(signupRequest));
    }

    @Test
    void registerUserThrowUserEmailAlreadyExistsException() {
        SignupRequest signupRequest = new SignupRequest();
        signupRequest.setUsername("test");
        signupRequest.setEmail("test@gmail.com");
        Mockito.when(userRepository.existsByEmail(signupRequest.getEmail())).thenReturn(true);
        assertThrows(UserEmailAlreadyExistsException.class, () -> authService.registerUser(signupRequest));
    }

}