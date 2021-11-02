package com.romantulchak.clouddisk.service;

import com.romantulchak.clouddisk.dto.FileDTO;
import com.romantulchak.clouddisk.model.*;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public interface FileService {

    List<FileDTO> findFilesInFolder(UUID folderLink);

    List<FileDTO> findFilesInDrive(String driveName);

    CompletableFuture<FileDTO> uploadFileIntoFolder(MultipartFile file, UUID folderLink, Authentication authentication);

    CompletableFuture<FileDTO> uploadFileIntoDrive(MultipartFile file, String driveName, Authentication authentication);

    ResponseEntity<Resource> downloadFile(UUID link) throws IOException;

    File getFile(MultipartFile multipartFile, User user, Drive drive, LocalPath path);

    File getFile(MultipartFile multipartFile, String fileName, User user, Folder folder);
}
