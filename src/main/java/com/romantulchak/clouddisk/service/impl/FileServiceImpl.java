package com.romantulchak.clouddisk.service.impl;

import com.mapperDTO.mapper.EntityMapperInvoker;
import com.romantulchak.clouddisk.dto.FileDTO;
import com.romantulchak.clouddisk.exception.DriveNotFoundException;
import com.romantulchak.clouddisk.exception.FileNotFoundException;
import com.romantulchak.clouddisk.exception.FolderNotFoundException;
import com.romantulchak.clouddisk.model.*;
import com.romantulchak.clouddisk.model.enums.RemoveType;
import com.romantulchak.clouddisk.repository.DriveRepository;
import com.romantulchak.clouddisk.repository.ElementAccessRepository;
import com.romantulchak.clouddisk.repository.FileRepository;
import com.romantulchak.clouddisk.repository.FolderRepository;
import com.romantulchak.clouddisk.service.FileService;
import com.romantulchak.clouddisk.utils.FileUtils;
import com.romantulchak.clouddisk.utils.FolderUtils;
import com.romantulchak.clouddisk.utils.StoreUtils;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Service
public class FileServiceImpl implements FileService {

    private final FileRepository fileRepository;
    private final FolderRepository folderRepository;
    private final FolderUtils folderUtils;
    private final DriveRepository driveRepository;
    private final ElementAccessRepository elementAccessRepository;
    private final EntityMapperInvoker<File, FileDTO> entityMapperInvoker;

    public FileServiceImpl(FileRepository fileRepository,
                           FolderRepository folderRepository,
                           FolderUtils folderUtils,
                           DriveRepository driveRepository,
                           ElementAccessRepository elementAccessRepository,
                           EntityMapperInvoker<File, FileDTO> entityMapperInvoker) {
        this.fileRepository = fileRepository;
        this.folderRepository = folderRepository;
        this.folderUtils = folderUtils;
        this.driveRepository = driveRepository;
        this.elementAccessRepository = elementAccessRepository;
        this.entityMapperInvoker = entityMapperInvoker;
    }

    @Override
    public List<FileDTO> findFilesInFolder(UUID folderLink) {
        return fileRepository.findAllByFolderLinkAndRemoveType(folderLink, RemoveType.SAVED)
                .stream()
                .map(file -> convertToDTO(file, View.FolderFileView.class))
                .collect(Collectors.toList());
    }

    @Override
    public List<FileDTO> findFilesInDrive(String driveName) {
        return fileRepository.findAllByDriveNameAndRemoveType(driveName, RemoveType.SAVED).stream()
                .sorted()
                .map(file -> convertToDTO(file, View.FolderFileView.class))
                .collect(Collectors.toList());
    }

    @Async
    @Override
    public CompletableFuture<FileDTO> uploadFileIntoFolder(MultipartFile multipartFile, UUID folderLink, Authentication authentication) {
        Folder folder = folderRepository.findFolderByLink(folderLink).orElseThrow((() -> new FolderNotFoundException(folderLink)));
        File file = getFile(multipartFile, multipartFile.getOriginalFilename(), folder.getOwner(), folder);
        if (folder.getAccess() != null) {
            ElementAccess elementAccess = new ElementAccess()
                    .setElement(file)
                    .setAccessType(folder.getAccess().getAccessType().name());
            elementAccessRepository.save(elementAccess);
            file.setAccess(elementAccess);
        }
        return CompletableFuture.completedFuture(convertToDTO(file, View.FolderFileView.class));
    }

    @Override
    public File getFile(MultipartFile multipartFile, String fileName, User user, Folder folder) {
        LocalPath path = folderUtils.uploadFile(multipartFile, folder.getPath().getShortPath());
        File file = new File()
                .setName(fileName)
                .setCreateAt(LocalDateTime.now())
                .setUploadAt(LocalDateTime.now())
                .setLink(UUID.randomUUID())
                .setOwner(user)
                .setSize(multipartFile.getSize())
                .setPath(path)
                .setFolder(folder);
        return fileRepository.save(file);

    }

    @Async
    @Override
    public CompletableFuture<FileDTO> uploadFileIntoDrive(MultipartFile multipartFile, String driveName, Authentication authentication) {
        Drive drive = driveRepository.findDriveByName(driveName)
                .orElseThrow(() -> new DriveNotFoundException(driveName));
        LocalPath path = folderUtils.uploadFile(multipartFile, drive.getShortPath());
        File file = getFile(multipartFile, drive.getOwner(), drive, path);
        fileRepository.save(file);
        return CompletableFuture.completedFuture(convertToDTO(file, View.FolderFileView.class));
    }

    public void test(String ...a){

    }

    @Override
    public File getFile(MultipartFile multipartFile, User user, Drive drive, LocalPath path) {
        return new File()
                .setDrive(drive)
                .setName(multipartFile.getOriginalFilename())
                .setCreateAt(LocalDateTime.now())
                .setUploadAt(LocalDateTime.now())
                .setLink(UUID.randomUUID())
                .setOwner(user)
                .setSize(multipartFile.getSize())
                .setPath(path);
    }

    @Override
    public ResponseEntity<Resource> downloadFile(UUID link) throws IOException {
        File file = fileRepository.findFileByLink(link).orElseThrow(() -> new FileNotFoundException(link));
        Path path = Paths.get(file.getPath().getShortPath());
        return FileUtils.getResource(path);
    }

    private FileDTO convertToDTO(File file, Class<?> classToCheck) {
        return entityMapperInvoker.entityToDTO(file, FileDTO.class, classToCheck)
                .setNoticed(StoreUtils.isStarred(file))
                .setOwner(StoreUtils.isOwner(file));
    }
}
