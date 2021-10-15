package com.romantulchak.clouddisk.access;

import com.romantulchak.clouddisk.exception.FolderNotFoundException;
import com.romantulchak.clouddisk.model.Folder;
import com.romantulchak.clouddisk.repository.FolderRepository;
import com.romantulchak.clouddisk.service.impl.UserDetailsImpl;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class UserFolderAccess {

    private final FolderRepository folderRepository;

    public UserFolderAccess(FolderRepository folderRepository) {
        this.folderRepository = folderRepository;
    }

    public boolean isAccessToSubFolder(UUID folderLink, Authentication authentication){
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        Folder folder = folderRepository.findFolderByLink(folderLink)
                .orElseThrow(() -> new FolderNotFoundException(folderLink));
        return folder.getOwner().getId() == userDetails.getId() || folder.isHasLinkAccess();
    }

}
