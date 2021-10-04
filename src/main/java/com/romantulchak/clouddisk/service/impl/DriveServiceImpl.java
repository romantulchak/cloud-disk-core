package com.romantulchak.clouddisk.service.impl;

import com.mapperDTO.mapper.EntityMapperInvoker;
import com.romantulchak.clouddisk.dto.DriveDTO;
import com.romantulchak.clouddisk.exception.DriveNotFoundException;
import com.romantulchak.clouddisk.exception.PlanWithTypeNotFoundException;
import com.romantulchak.clouddisk.model.*;
import com.romantulchak.clouddisk.model.enums.PlanType;
import com.romantulchak.clouddisk.repository.DriveRepository;
import com.romantulchak.clouddisk.repository.PlanRepository;
import com.romantulchak.clouddisk.service.DriveService;
import com.romantulchak.clouddisk.utils.FolderUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class DriveServiceImpl implements DriveService {

    private final DriveRepository driveRepository;
    private final PlanRepository planRepository;
    private final FolderUtils folderUtils;
    private final EntityMapperInvoker<Drive, DriveDTO> entityMapperInvoker;

    @Autowired
    public DriveServiceImpl(DriveRepository driveRepository,
                            PlanRepository planRepository,
                            FolderUtils folderUtils,
                            EntityMapperInvoker<Drive, DriveDTO> entityMapperInvoker) {
        this.driveRepository = driveRepository;
        this.planRepository = planRepository;
        this.folderUtils = folderUtils;
        this.entityMapperInvoker = entityMapperInvoker;
    }

    @Override
    public void create(User user) {
        String driveName = user.getUsername() + "-" + UUID.randomUUID().toString().replace("-", "");
        Plan plan = planRepository.findPlanByName(PlanType.STANDARD).orElseThrow(() -> new PlanWithTypeNotFoundException(PlanType.STANDARD));
        Drive drive = new Drive()
                .setName(driveName)
                .setOwner(user)
                .setPlan(plan)
                .setCreateAt(LocalDateTime.now());
        LocalPath localPath = folderUtils.createDrive(driveName);
        drive.setFullPath(localPath.getFullPath())
                .setShortPath((localPath.getShortPath()));
        driveRepository.save(drive);
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
