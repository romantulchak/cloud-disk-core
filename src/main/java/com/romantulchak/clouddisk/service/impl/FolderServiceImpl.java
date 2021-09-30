package com.romantulchak.clouddisk.service.impl;

import com.mapperDTO.mapper.EntityMapperInvoker;
import com.romantulchak.clouddisk.dto.FolderDTO;
import com.romantulchak.clouddisk.exception.DriveNotFoundException;
import com.romantulchak.clouddisk.model.Drive;
import com.romantulchak.clouddisk.model.Folder;
import com.romantulchak.clouddisk.model.View;
import com.romantulchak.clouddisk.repository.DriveRepository;
import com.romantulchak.clouddisk.repository.FolderRepository;
import com.romantulchak.clouddisk.service.FolderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
    public void create(String folderName, String driveName) {
        Drive drive = driveRepository.findDriveByName(driveName)
                .orElseThrow(() -> new DriveNotFoundException(driveName));
        Folder folder = new Folder()
                .setName(folderName)
                .setOwner(drive.getOwner())
                .setDrive(drive)
                .setLink(UUID.randomUUID())
                .setCreateAt(LocalDateTime.now())
                .setUploadAt(LocalDateTime.now());
        folderRepository.save(folder);
    }

    @Override
    public List<FolderDTO> findAllFoldersForDrive(String driveName) {
        return folderRepository.findAllByDriveName(driveName).stream()
                .map(folder -> convertToDTO(folder, View.FolderView.class))
                .collect(Collectors.toList());
    }

    private FolderDTO convertToDTO(Folder folder, Class<?> classToCheck){
        return entityMapperInvoker.entityToDTO(folder, FolderDTO.class, classToCheck);
    }


}
