package com.romantulchak.clouddisk.service;

import com.romantulchak.clouddisk.dto.FolderDTO;
import com.romantulchak.clouddisk.model.Store;
import org.springframework.security.core.Authentication;

import java.util.List;
import java.util.UUID;

public interface FolderService {

    FolderDTO create(String folderName, String driveName, Authentication authentication);

    List<Store> findAllFoldersForDrive(String drive);

    FolderDTO createSubFolder(String folderName, UUID folderLink, Authentication authentication);

    List<Store> findSubFoldersInFolder(UUID folderLink);

    void removeFolder(UUID folderLink);
}
