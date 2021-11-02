package com.romantulchak.clouddisk.service;

import com.romantulchak.clouddisk.dto.FolderDTO;
import com.romantulchak.clouddisk.dto.StoreAbstractDTO;
import com.romantulchak.clouddisk.model.Folder;
import com.romantulchak.clouddisk.model.Store;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

public interface FolderService {

    FolderDTO create(String folderName, String driveName, Authentication authentication);

    Folder findFolderByLink(UUID folderLink);

    List<Store> findAllFoldersForDrive(String drive);

    FolderDTO createSubFolder(String folderName, UUID folderLink, Authentication authentication);

    List<StoreAbstractDTO> findSubFoldersInFolder(UUID folderLink);

    ResponseEntity<Resource> downloadFolder(UUID folderLink) throws IOException;

    FolderDTO changeColor(UUID folderLink, String color);

    FolderDTO uploadIntoDrive(List<MultipartFile> files, String driveName);
}
