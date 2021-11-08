package com.romantulchak.clouddisk.service.impl;

import com.romantulchak.clouddisk.model.User;
import com.romantulchak.clouddisk.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class UserDetailsServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserDetailsServiceImpl userDetailsService;

    @Test
    void loadUserByUsername() {
        String username = "test";
        User user = new User("firstName", "lastName", username, "test@email.com", "test");
        user.setRoles(new HashSet<>());
        Mockito.when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));
        UserDetailsImpl userDetails = (UserDetailsImpl) userDetailsService.loadUserByUsername(username);
        assertNotNull(userDetails);
        assertEquals(user.getUsername(), userDetails.getUsername());
        assertEquals(0, userDetails.getAuthorities().size());
        assertEquals(user.getEmail(), userDetails.getEmail());
    }

    @Test
    void loadUserByUsernameThrowUsernameNotFoundException() {
        String username = "test";
        Mockito.when(userRepository.findByUsername(username)).thenThrow(UsernameNotFoundException.class);
        assertThrows(UsernameNotFoundException.class, () -> userDetailsService.loadUserByUsername(username));
    }
}