package com.romantulchak.clouddisk.access;

import com.romantulchak.clouddisk.repository.FileRepository;
import com.romantulchak.clouddisk.service.impl.UserDetailsImpl;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class UserFileAccess {

    private final FileRepository fileRepository;

    public UserFileAccess(FileRepository fileRepository) {
        this.fileRepository = fileRepository;
    }

    public boolean hasAccess(UUID fileLink, Authentication authentication){
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        return fileRepository.existsByOwnerIdAndLink(userDetails.getId(), fileLink);
    }
}
