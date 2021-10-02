package com.romantulchak.clouddisk.service.impl;

import com.mapperDTO.mapper.EntityMapperInvoker;
import com.romantulchak.clouddisk.dto.FileDTO;
import com.romantulchak.clouddisk.dto.FolderDTO;
import com.romantulchak.clouddisk.exception.FolderNotFoundException;
import com.romantulchak.clouddisk.model.*;
import com.romantulchak.clouddisk.repository.DriveRepository;
import com.romantulchak.clouddisk.repository.FolderRepository;
import com.romantulchak.clouddisk.service.FileService;
import com.romantulchak.clouddisk.service.FolderService;
import com.romantulchak.clouddisk.utils.FolderUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class FolderServiceImpl implements FolderService {

    private final FolderRepository folderRepository;
    private final DriveRepository driveRepository;
    private final FileService fileService;
    private final EntityMapperInvoker<Folder, FolderDTO> entityMapperInvoker;

    @Value("${cloud.disk.files.folder}")
    private String drivePath;

    @Value("${cloud.disk.host}")
    private String host;

    @Autowired
    public FolderServiceImpl(FolderRepository folderRepository,
                             DriveRepository driveRepository,
                             FileService fileService,
                             EntityMapperInvoker<Folder, FolderDTO> entityMapperInvoker) {
        this.folderRepository = folderRepository;
        this.driveRepository = driveRepository;
        this.fileService = fileService;
        this.entityMapperInvoker = entityMapperInvoker;
    }

    @Override
    public FolderDTO create(String folderName, String driveName, Authentication authentication) {
        Folder folder = getFolder(folderName, driveName, authentication);
        return convertToDTO(folder, View.FolderView.class);
    }

    private Folder getFolder(String folderName, String driveName, Authentication authentication) {
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        User user = new User(userDetails.getId(), userDetails.getFirstName(), userDetails.getLastName());
        Drive drive = driveRepository.findDriveByName(driveName).orElse(null);
        Folder folder = new Folder()
                .setName(folderName)
                .setOwner(user)
                .setDrive(drive)
                .setLink(UUID.randomUUID())
                .setCreateAt(LocalDateTime.now())
                .setUploadAt(LocalDateTime.now());
        FolderUtils folderUtils = new FolderUtils(drivePath, host);
        LocalPath path;
        if (drive != null){
            path = folderUtils.createFolder(folderName, drive);
        }

        folder = folderRepository.save(folder);
        return folder;
    }

    @Override
    public List<Store> findAllFoldersForDrive(String driveName) {
        List<FolderDTO> folders = folderRepository.findAllByDriveName(driveName).stream()
                .map(folder -> convertToDTO(folder, View.FolderFileView.class))
                .collect(Collectors.toList());
        List<FileDTO> files = fileService.findFilesInDrive(driveName);
        List<Store> stores = new ArrayList<>();
        stores.addAll(folders);
        stores.addAll(files);
        return stores;
    }

    @Transactional
    @Override
    public FolderDTO createSubFolder(String folderName, UUID folderLink, Authentication authentication) {
        Folder mainFolder = folderRepository.findFolderByLink(folderLink)
                .orElseThrow(() -> new FolderNotFoundException(folderLink.toString()));
        Folder subFolder = getFolder(folderName, null, authentication);
        mainFolder.getSubFolders().add(subFolder);
        return convertToDTO(subFolder, View.FolderView.class);
    }

    @Override
    public List<Store> findSubFoldersInFolder(UUID folderLink) {
        List<FolderDTO> subFolders = folderRepository.findSubFolders(folderLink).stream()
                .map(folder -> convertToDTO(folder, View.FolderFileView.class))
                .collect(Collectors.toList());
        List<FileDTO> filesInFolder = fileService.findFilesInFolder(folderLink);
        List<Store> stores = new ArrayList<>();
        stores.addAll(subFolders);
        stores.addAll(filesInFolder);
        return stores;
    }

    @Override
    public void removeFolder(UUID folderLink) {
        folderRepository.deleteFolderByLink(folderLink);
    }

    private FolderDTO convertToDTO(Folder folder, Class<?> classToCheck) {
        return entityMapperInvoker.entityToDTO(folder, FolderDTO.class, classToCheck);
    }

}
