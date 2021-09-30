package com.romantulchak.clouddisk.service.impl;

import com.romantulchak.clouddisk.exception.RoleNotFoundException;
import com.romantulchak.clouddisk.exception.UserEmailAlreadyExistsException;
import com.romantulchak.clouddisk.exception.UsernameAlreadyExistsException;
import com.romantulchak.clouddisk.model.Role;
import com.romantulchak.clouddisk.model.User;
import com.romantulchak.clouddisk.model.enums.RoleType;
import com.romantulchak.clouddisk.payload.request.LoginRequest;
import com.romantulchak.clouddisk.payload.request.SignupRequest;
import com.romantulchak.clouddisk.payload.response.JwtResponse;
import com.romantulchak.clouddisk.repository.RoleRepository;
import com.romantulchak.clouddisk.repository.UserRepository;
import com.romantulchak.clouddisk.security.jwt.JwtUtils;
import com.romantulchak.clouddisk.service.AuthService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class AuthServiceImpl implements AuthService {

    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder encoder;
    private final JwtUtils jwtUtils;


    public AuthServiceImpl(AuthenticationManager authenticationManager,
                           UserRepository userRepository,
                           RoleRepository roleRepository,
                           PasswordEncoder encoder,
                           JwtUtils jwtUtils) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.encoder = encoder;
        this.jwtUtils = jwtUtils;
    }

    @Override
    public JwtResponse authenticateUser(LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        List<String> roles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());
        return new JwtResponse(jwt,
                userDetails.getId(),
                userDetails.getUsername(),
                userDetails.getEmail(),
                roles);
    }

    @Transactional
    @Override
    public void registerUser(SignupRequest signupRequest) {
        if(userRepository.existsByUsername(signupRequest.getUsername())){
            throw new UsernameAlreadyExistsException(signupRequest.getUsername());
        }
        if(userRepository.existsByEmail(signupRequest.getEmail())){
            throw new UserEmailAlreadyExistsException(signupRequest.getEmail());
        }
        User user = new User(signupRequest.getFirstName(),
                             signupRequest.getLastName(),
                             signupRequest.getUsername(),
                             signupRequest.getEmail(),
                             encoder.encode(signupRequest.getPassword()));
        Set<Role> roles = new HashSet<>();
        Role role = roleRepository.findByName(RoleType.ROLE_USER)
                .orElseThrow(() -> new RoleNotFoundException(RoleType.ROLE_USER));
        roles.add(role);
        user.setRoles(roles);
        userRepository.save(user);
    }
}
