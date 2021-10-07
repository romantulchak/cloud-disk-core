package com.romantulchak.clouddisk.service.impl;

import com.mapperDTO.mapper.EntityMapperInvoker;
import com.romantulchak.clouddisk.dto.FileDTO;
import com.romantulchak.clouddisk.exception.DriveNotFoundException;
import com.romantulchak.clouddisk.exception.FileNotFoundException;
import com.romantulchak.clouddisk.exception.FileWithNameAlreadyExistsException;
import com.romantulchak.clouddisk.exception.FolderNotFoundException;
import com.romantulchak.clouddisk.model.*;
import com.romantulchak.clouddisk.model.enums.RemoveType;
import com.romantulchak.clouddisk.repository.DriveRepository;
import com.romantulchak.clouddisk.repository.FileRepository;
import com.romantulchak.clouddisk.repository.FolderRepository;
import com.romantulchak.clouddisk.service.FileService;
import com.romantulchak.clouddisk.service.TrashService;
import com.romantulchak.clouddisk.utils.FileUtils;
import com.romantulchak.clouddisk.utils.FolderUtils;
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
    private final TrashService trashService;
    private final EntityMapperInvoker<File, FileDTO> entityMapperInvoker;

    public FileServiceImpl(FileRepository fileRepository,
                           FolderRepository folderRepository,
                           FolderUtils folderUtils,
                           TrashService trashService,
                           DriveRepository driveRepository,
                           EntityMapperInvoker<File, FileDTO> entityMapperInvoker) {
        this.fileRepository = fileRepository;
        this.folderRepository = folderRepository;
        this.folderUtils = folderUtils;
        this.driveRepository = driveRepository;
        this.trashService = trashService;
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
                .map(file -> convertToDTO(file, View.FolderFileView.class))
                .collect(Collectors.toList());
    }

    @Override
    public List<FileDTO> findRemovedFilesByTrashId(long id) {
        return fileRepository.findAllByTrashId(id).stream()
                .map(file -> convertToDTO(file, View.FolderFileView.class))
                .collect(Collectors.toList());
    }

    @Async
    @Override
    public CompletableFuture<FileDTO> uploadFileIntoFolder(MultipartFile multipartFile, UUID folderLink, Authentication authentication) {
        isFileNameExists(multipartFile.getOriginalFilename());
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        Folder folder = folderRepository.findFolderByLink(folderLink).orElseThrow((() -> new FolderNotFoundException(folderLink)));
        LocalPath path = folderUtils.uploadFile(multipartFile, folder.getPath().getShortPath());
        File file = new File()
                .setName(multipartFile.getOriginalFilename())
                .setCreateAt(LocalDateTime.now())
                .setUploadAt(LocalDateTime.now())
                .setExtension(FileUtils.getFileExtension(multipartFile.getOriginalFilename()))
                .setLink(UUID.randomUUID())
                .setOwner(new User(userDetails.getId(), userDetails.getFirstName(), userDetails.getLastName()))
                .setSize(multipartFile.getSize())
                .setPath(path)
                .setFolder(folder);
        fileRepository.save(file);
        return CompletableFuture.completedFuture(convertToDTO(file, View.FolderFileView.class));
    }

    @Async
    @Override
    public CompletableFuture<FileDTO> uploadFileIntoDrive(MultipartFile multipartFile, String driveName, Authentication authentication) {
        isFileNameExists(multipartFile.getOriginalFilename());
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        Drive drive = driveRepository.findDriveByName(driveName)
                .orElseThrow(() -> new DriveNotFoundException(driveName));
        LocalPath path = folderUtils.uploadFile(multipartFile, drive.getShortPath());
        File file = new File()
                .setDrive(drive)
                .setName(multipartFile.getOriginalFilename())
                .setCreateAt(LocalDateTime.now())
                .setUploadAt(LocalDateTime.now())
                .setExtension(FileUtils.getFileExtension(multipartFile.getOriginalFilename()))
                .setLink(UUID.randomUUID())
                .setOwner(new User(userDetails.getId(), userDetails.getFirstName(), userDetails.getLastName()))
                .setSize(multipartFile.getSize())
                .setPath(path);
        fileRepository.save(file);
        return CompletableFuture.completedFuture(convertToDTO(file, View.FolderFileView.class));
    }

    @Override
    public void deleteFileInFolder(UUID fileLink) {
        File file = fileRepository.findFileByLink(fileLink).orElseThrow(() -> new FileNotFoundException(fileLink));
        boolean isDeleted = folderUtils.removeElement(file.getPath().getShortPath());
        if (isDeleted) {
            fileRepository.delete(file);
        }
    }

    @Override
    public void preDeleteFile(UUID fileLink, String driveName) {
        File file = fileRepository.findFileByLink(fileLink)
                .orElseThrow(() -> new FileNotFoundException(fileLink));
        Trash trash = trashService.getTrashByDriveName(driveName);
        file.setRemoveType(RemoveType.PRE_REMOVED)
                .setTrash(trash);
        boolean isMoved = folderUtils.moveFileToTrash(file.getPath().getShortPath(), trash.getPath().getShortPath());
        if (isMoved) {
            fileRepository.save(file);
        }
    }

    @Override
    public ResponseEntity<Resource> downloadFile(UUID link) throws IOException {
        File file = fileRepository.findFileByLink(link).orElseThrow(() -> new FileNotFoundException(link));
        Path path = Paths.get(file.getPath().getShortPath());
        return FileUtils.getResource(path, file.getPath().getShortPath());
    }

    private void isFileNameExists(String fileName) {
        boolean isExists = fileRepository.existsByName(fileName);
        if (isExists) {
            throw new FileWithNameAlreadyExistsException(fileName);
        }
    }

    private FileDTO convertToDTO(File file, Class<?> classToCheck) {
        return entityMapperInvoker.entityToDTO(file, FileDTO.class, classToCheck);
    }
}
