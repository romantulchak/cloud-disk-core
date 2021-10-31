package com.romantulchak.clouddisk.service.impl;

import com.mapperDTO.mapper.EntityMapperInvoker;
import com.romantulchak.clouddisk.dto.FileDTO;
import com.romantulchak.clouddisk.dto.FolderDTO;
import com.romantulchak.clouddisk.dto.StoreAbstractDTO;
import com.romantulchak.clouddisk.exception.DriveNotFoundException;
import com.romantulchak.clouddisk.exception.FolderNotFoundException;
import com.romantulchak.clouddisk.exception.TrashNotFoundException;
import com.romantulchak.clouddisk.model.*;
import com.romantulchak.clouddisk.model.enums.RemoveType;
import com.romantulchak.clouddisk.repository.*;
import com.romantulchak.clouddisk.service.FileService;
import com.romantulchak.clouddisk.service.FolderService;
import com.romantulchak.clouddisk.utils.FileUtils;
import com.romantulchak.clouddisk.utils.FolderUtils;
import com.romantulchak.clouddisk.utils.StoreUtils;
import com.romantulchak.clouddisk.utils.ZipUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
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
    private final FolderUtils folderUtils;
    private final ElementAccessRepository elementAccessRepository;
    private final EntityMapperInvoker<Folder, FolderDTO> entityMapperInvoker;


    @Autowired
    public FolderServiceImpl(FolderRepository folderRepository,
                             DriveRepository driveRepository,
                             FileService fileService,
                             FolderUtils folderUtils,
                             TrashRepository trashRepository,
                             PreRemoveRepository removeRepository,
                             ElementAccessRepository elementAccessRepository,
                             EntityMapperInvoker<Folder, FolderDTO> entityMapperInvoker) {
        this.folderRepository = folderRepository;
        this.driveRepository = driveRepository;
        this.fileService = fileService;
        this.folderUtils = folderUtils;
        this.entityMapperInvoker = entityMapperInvoker;
        this.elementAccessRepository = elementAccessRepository;
    }

    //TODO: add check if folder named doesn't exist
    @Override
    public FolderDTO create(String folderName, String driveName, Authentication authentication) {
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        User user = new User(userDetails.getId(), userDetails.getFirstName(), userDetails.getLastName());
        Drive drive = driveRepository.findDriveByName(driveName)
                .orElseThrow(() -> new DriveNotFoundException(user.getUsername()));
        Folder folder = getFolder(folderName, user, drive.getShortPath())
                .setDrive(drive);
        folder = folderRepository.save(folder);
        return convertToDTO(folder, View.FolderView.class);
    }

    @Override
    public Folder findFolderByLink(UUID folderLink) {
        return folderRepository.findFolderByLink(folderLink)
                .orElseThrow((() -> new FolderNotFoundException(folderLink)));
    }

    private Folder getFolder(String folderName, User user, String shortPath) {
        Folder folder = new Folder()
                .setName(folderName)
                .setOwner(user)
                .setLink(UUID.randomUUID())
                .setCreateAt(LocalDateTime.now())
                .setUploadAt(LocalDateTime.now());
        LocalPath path = folderUtils.createFolder(folderName, shortPath);
        folder.setPath(path);
        return folder;
    }

    private Folder getSubFolder(String folderName, Authentication authentication, String shortPath) {
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        User user = new User(userDetails.getId(), userDetails.getFirstName(), userDetails.getLastName());
        return getFolder(folderName, user, shortPath);

    }

    @Override
    public List<Store> findAllFoldersForDrive(String driveName) {
        List<FolderDTO> folders = folderRepository.findAllByDriveNameAndRemoveType(driveName, RemoveType.SAVED)
                .stream()
                .sorted()
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
                .orElseThrow(() -> new FolderNotFoundException(folderLink));
        Folder subFolder = getSubFolder(folderName, authentication, mainFolder.getPath().getShortPath());
        subFolder = folderRepository.save(subFolder);
        if (mainFolder.getAccess() != null){
            ElementAccess elementAccess = new ElementAccess()
                    .setElement(subFolder)
                    .setAccessType(mainFolder.getAccess().getAccessType().name());
            subFolder.setAccess(elementAccess);
            elementAccessRepository.save(elementAccess);
        }
        mainFolder.getSubFolders().add(subFolder);
        return convertToDTO(subFolder, View.FolderView.class);
    }

    @Override
    public List<StoreAbstractDTO> findSubFoldersInFolder(UUID folderLink) {
        List<FolderDTO> subFolders = folderRepository.findSubFolders(folderLink, RemoveType.SAVED.name())
                .stream()
                .sorted()
                .map(folder -> convertToDTO(folder, View.FolderFileView.class))
                .collect(Collectors.toList());
        List<FileDTO> filesInFolder = fileService.findFilesInFolder(folderLink);
        List<StoreAbstractDTO> stores = new ArrayList<>();
        stores.addAll(subFolders);
        stores.addAll(filesInFolder);
        return stores;
    }

    @Override
    public ResponseEntity<Resource> downloadFolder(UUID folderLink) throws IOException {
        Folder folder = folderRepository.findFolderByLink(folderLink)
                .orElseThrow(() -> new FolderNotFoundException(folderLink));
        Path path = ZipUtils.createZip(folder.getName(), folder.getPath().getShortPath());
        return FileUtils.getResource(path, folder.getPath().getShortPath());
    }

    @Override
    public FolderDTO changeColor(UUID folderLink, String color) {
        Folder folder = findFolderByLink(folderLink);
        if(!folder.getColor().equals(color)){
            folder.setColor(color);
            folderRepository.save(folder);
        }
        return convertToDTO(folder, View.FolderFileView.class);
    }

    private FolderDTO convertToDTO(Folder folder, Class<?> classToCheck) {
        return entityMapperInvoker.entityToDTO(folder, FolderDTO.class, classToCheck)
                .setNoticed(StoreUtils.isStarred(folder))
                .setOwner(StoreUtils.isOwner(folder));
    }

}
