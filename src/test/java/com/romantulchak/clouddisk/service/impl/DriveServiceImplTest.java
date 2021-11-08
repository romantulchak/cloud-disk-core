package com.romantulchak.clouddisk.service.impl;

import com.mapperDTO.mapper.EntityMapper;
import com.mapperDTO.mapper.EntityMapperInvoker;
import com.romantulchak.clouddisk.dto.DriveDTO;
import com.romantulchak.clouddisk.exception.DriveNotFoundException;
import com.romantulchak.clouddisk.exception.PlanWithTypeNotFoundException;
import com.romantulchak.clouddisk.model.Drive;
import com.romantulchak.clouddisk.model.LocalPath;
import com.romantulchak.clouddisk.model.Plan;
import com.romantulchak.clouddisk.model.User;
import com.romantulchak.clouddisk.model.enums.PlanType;
import com.romantulchak.clouddisk.repository.DriveRepository;
import com.romantulchak.clouddisk.repository.PlanRepository;
import com.romantulchak.clouddisk.utils.FolderUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DriveServiceImplTest {

    @Mock
    private Authentication authentication;

    @Mock
    private PlanRepository planRepository;

    @Mock
    private DriveRepository driveRepository;

    @Mock
    private FolderUtils folderUtils;

    @InjectMocks
    private EntityMapperInvoker<Drive, DriveDTO> entityMapperInvoker;

    @InjectMocks
    private DriveServiceImpl driveService;

    @BeforeEach
    public void setUP(){
        EntityMapper entityMapper = new EntityMapper();
        ReflectionTestUtils.setField(entityMapperInvoker,"entityMapper", entityMapper);
        ReflectionTestUtils.setField(driveService, "entityMapperInvoker", entityMapperInvoker);
    }

    @Test
    void createThrowDrivePlanNotFoundException() {
        User user = new User();
        user.setUsername(anyString());
        assertThrows(PlanWithTypeNotFoundException.class, () -> driveService.create(user));
    }

    @Test
    void create() {
        LocalPath localPath = new LocalPath("testpath", "localhost:8080/stestpath");
        User user = new User();
        user.setUsername("Test");
        Optional<Plan> plan = Optional.of(new Plan(PlanType.STANDARD));
        when(planRepository.findPlanByName(PlanType.STANDARD)).thenReturn(plan);
        when(folderUtils.createDrive(user.getUsername())).thenReturn(localPath);
        driveService.create(user);
        verify(driveRepository, times(1)).save(any());
    }

    @Test
    void findUserDriveThrowDriveNotFoundException() {
        UserDetailsImpl userDetails = new UserDetailsImpl(1L, "Test", "Test", "test", "test@gmail.com", "test", null);
        when(authentication.getPrincipal()).thenReturn(userDetails);
        assertThrows(DriveNotFoundException.class, () -> driveService.findUserDrive(authentication));
    }

    @Test
    void findUserDrive() {
        Optional<Drive> drive = Optional.of(new Drive()
                .setId(1)
                .setName("Test"));
        DriveDTO driveDTO = new DriveDTO();
        driveDTO.setId(drive.get().getId());
        driveDTO.setName(drive.get().getName());
        UserDetailsImpl userDetails = new UserDetailsImpl(1L, "Test", "Test", "test", "test@gmail.com", "test", null);
        when(authentication.getPrincipal()).thenReturn(userDetails);
        when(driveRepository.findDriveByOwnerId(userDetails.getId())).thenReturn(drive);
        DriveDTO userDrive = driveService.findUserDrive(authentication);
        assertNotNull(userDrive);
        assertEquals(driveDTO.getId(), userDrive.getId());
        assertEquals(driveDTO.getName(), userDrive.getName());
    }
}