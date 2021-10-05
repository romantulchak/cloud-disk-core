package com.romantulchak.clouddisk.service;

import com.romantulchak.clouddisk.dto.FileDTO;
import org.springframework.security.core.Authentication;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public interface FileService {

    List<FileDTO> findFilesInFolder(UUID folderLink);

    List<FileDTO> findFilesInDrive(String driveName);

    CompletableFuture<FileDTO> uploadFileIntoFolder(MultipartFile file, UUID folderLink, Authentication authentication);

    CompletableFuture<FileDTO> uploadFileIntoDrive(MultipartFile file, String driveName, Authentication authentication);

    void deleteFileInFolder(UUID fileLink);
}