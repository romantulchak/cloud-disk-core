package com.romantulchak.clouddisk.access;

import com.romantulchak.clouddisk.repository.DriveRepository;
import com.romantulchak.clouddisk.service.impl.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

@Component
public class UserDriverAccess {

    private final DriveRepository driveRepository;

    @Autowired
    public UserDriverAccess(DriveRepository driveRepository) {
        this.driveRepository = driveRepository;
    }

    public boolean checkAccess(Authentication authentication, String name) {
        UserDetailsImpl user = (UserDetailsImpl) authentication.getPrincipal();
        if (user == null) {
            return false;
        }
        return driveRepository.existsByOwnerIdAndName(user.getId(), name);
    }

}
