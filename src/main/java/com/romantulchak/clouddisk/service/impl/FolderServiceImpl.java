package com.romantulchak.clouddisk.service.impl;

import com.mapperDTO.mapper.EntityMapperInvoker;
import com.romantulchak.clouddisk.dto.FolderDTO;
import com.romantulchak.clouddisk.exception.FolderNotFoundException;
import com.romantulchak.clouddisk.model.Drive;
import com.romantulchak.clouddisk.model.Folder;
import com.romantulchak.clouddisk.model.User;
import com.romantulchak.clouddisk.model.View;
import com.romantulchak.clouddisk.repository.DriveRepository;
import com.romantulchak.clouddisk.repository.FolderRepository;
import com.romantulchak.clouddisk.service.FolderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class FolderServiceImpl implements FolderService {

    private final FolderRepository folderRepository;
    private final DriveRepository driveRepository;
    private final EntityMapperInvoker<Folder, FolderDTO> entityMapperInvoker;


    @Autowired
    public FolderServiceImpl(FolderRepository folderRepository,
                             DriveRepository driveRepository,
                             EntityMapperInvoker<Folder, FolderDTO> entityMapperInvoker) {
        this.folderRepository = folderRepository;
        this.driveRepository = driveRepository;
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
        folder = folderRepository.save(folder);
        return folder;
    }

    @Override
    public List<FolderDTO> findAllFoldersForDrive(String driveName) {
        return folderRepository.findAllByDriveName(driveName).stream()
                .map(folder -> convertToDTO(folder, View.FolderView.class))
                .collect(Collectors.toList());
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
    public List<FolderDTO> findSubFoldersInFolder(UUID folderLink) {
        return folderRepository.findSubFolders(folderLink).stream()
                .map(folder -> convertToDTO(folder, View.FolderView.class))
                .collect(Collectors.toList());
    }

    @Override
    public void removeFolder(UUID folderLink) {
        folderRepository.deleteFolderByLink(folderLink);
    }

    private FolderDTO convertToDTO(Folder folder, Class<?> classToCheck) {
        return entityMapperInvoker.entityToDTO(folder, FolderDTO.class, classToCheck);
    }

}
