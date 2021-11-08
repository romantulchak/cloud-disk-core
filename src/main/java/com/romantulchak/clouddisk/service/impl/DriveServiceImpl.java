package com.romantulchak.clouddisk.service.impl;

import com.mapperDTO.mapper.EntityMapperInvoker;
import com.romantulchak.clouddisk.constant.FilenameConstant;
import com.romantulchak.clouddisk.dto.DriveDTO;
import com.romantulchak.clouddisk.exception.DriveNotFoundException;
import com.romantulchak.clouddisk.exception.PlanWithTypeNotFoundException;
import com.romantulchak.clouddisk.model.*;
import com.romantulchak.clouddisk.model.enums.PlanType;
import com.romantulchak.clouddisk.repository.DriveRepository;
import com.romantulchak.clouddisk.repository.PlanRepository;
import com.romantulchak.clouddisk.service.DriveService;
import com.romantulchak.clouddisk.service.TrashService;
import com.romantulchak.clouddisk.utils.FolderUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

@Service
public class DriveServiceImpl implements DriveService {

    private final DriveRepository driveRepository;
    private final PlanRepository planRepository;
    private final FolderUtils folderUtils;
    private final TrashService trashService;
    private final EntityMapperInvoker<Drive, DriveDTO> entityMapperInvoker;

    public DriveServiceImpl(DriveRepository driveRepository,
                            PlanRepository planRepository,
                            FolderUtils folderUtils,
                            TrashService trashService,
                            EntityMapperInvoker<Drive, DriveDTO> entityMapperInvoker) {
        this.driveRepository = driveRepository;
        this.planRepository = planRepository;
        this.folderUtils = folderUtils;
        this.trashService = trashService;
        this.entityMapperInvoker = entityMapperInvoker;
    }

    @Transactional
    @Override
    public void create(User user) {
        String driveName = user.getUsername() + FilenameConstant.MINUS_SEPARATOR + UUID.randomUUID()
                .toString().replace(FilenameConstant.MINUS_SEPARATOR, "");
        Plan plan = planRepository.findPlanByName(PlanType.STANDARD)
                .orElseThrow(() -> new PlanWithTypeNotFoundException(PlanType.STANDARD));
        LocalPath localPath = folderUtils.createDrive(driveName);
        LocalDateTime createAt = LocalDateTime.now()
                .withSecond(0)
                .withNano(0);
        Drive drive = new Drive()
                .setName(driveName)
                .setOwner(user)
                .setPlan(plan)
                .setCreateAt(createAt)
                .setFullPath(localPath.getFullPath())
                .setShortPath((localPath.getShortPath()));
        driveRepository.save(drive);
        trashService.create(drive);
    }

    @Override
    public DriveDTO findUserDrive(Authentication authentication) {
        UserDetailsImpl user = (UserDetailsImpl) authentication.getPrincipal();
        Drive drive = driveRepository.findDriveByOwnerId(user.getId())
                .orElseThrow(() -> new DriveNotFoundException(user.getUsername()));
        return convertToDTO(drive, View.DriveView.class);
    }

    private DriveDTO convertToDTO(Drive drive, Class<?> classToCheck) {
        return entityMapperInvoker.entityToDTO(drive, DriveDTO.class, classToCheck);
    }
}
