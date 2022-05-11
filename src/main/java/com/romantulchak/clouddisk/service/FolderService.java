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
import java.util.concurrent.CompletableFuture;

public interface FolderService {

    FolderDTO create(String folderName, String driveName, Authentication authentication);

    Folder findFolderByLink(UUID folderLink);

    FolderDTO createSubFolder(String folderName, UUID folderLink, Authentication authentication);

    List<Store> findSubFoldersInFolder(UUID folderLink, String page);

    ResponseEntity<Resource> downloadFolder(UUID folderLink) throws IOException;

    FolderDTO changeColor(UUID folderLink, String color);

    CompletableFuture<FolderDTO> uploadInDrive(List<MultipartFile> files, String driveName);

    CompletableFuture<FolderDTO> uploadInFolder(List<MultipartFile> files, String folderLink);
}
