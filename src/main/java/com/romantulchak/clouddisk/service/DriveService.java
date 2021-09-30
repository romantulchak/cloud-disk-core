package com.romantulchak.clouddisk.service;

import com.romantulchak.clouddisk.dto.DriveDTO;
import com.romantulchak.clouddisk.model.User;
import org.springframework.security.core.Authentication;

public interface DriveService {
    void create(User user);

    DriveDTO findUserDrive(Authentication authentication);
}
