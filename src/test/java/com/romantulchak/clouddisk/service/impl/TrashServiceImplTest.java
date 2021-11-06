package com.romantulchak.clouddisk.service.impl;

import com.romantulchak.clouddisk.exception.TrashNotFoundException;
import com.romantulchak.clouddisk.model.Drive;
import com.romantulchak.clouddisk.model.LocalPath;
import com.romantulchak.clouddisk.model.Trash;
import com.romantulchak.clouddisk.repository.TrashRepository;
import com.romantulchak.clouddisk.utils.FolderUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class TrashServiceImplTest {

    @Mock
    private TrashRepository trashRepository;

    @Mock
    private FolderUtils folderUtils;

    @InjectMocks
    private TrashServiceImpl trashService;

    @Test
    void create() {
        String path = "path";
        Drive drive = new Drive()
                .setName("Test")
                .setCreateAt(LocalDateTime.now())
                        .setShortPath(path);
        Trash trash = new Trash()
                .setName("Trash-" + drive.getName())
                .setDrive(drive)
                .setPath(path);
        Mockito.when(folderUtils.createTrash(trash.getName(), drive.getShortPath())).thenReturn(path);
        Mockito.when(trashRepository.save(Mockito.any(Trash.class))).thenReturn(trash);
        Trash trashAfterSave = trashService.create(drive);
        assertNotNull(trashAfterSave);
        assertEquals(trash.getName(), trashAfterSave.getName());
        assertEquals(trash.getDrive(), trashAfterSave.getDrive());
        assertEquals(trash.getPath(), trashAfterSave.getPath());
    }

    @Test
    void getTrashByDriveNameThrowTrashNotFoundException() {
        String driveName = "Test";
        assertThrows(TrashNotFoundException.class, () -> trashService.getTrashByDriveName(driveName));
    }

    @Test
    void getTrashByDriveName() {
        String driveName = "Test";
        Trash trash = new Trash()
                .setId(1)
                .setName(driveName)
                        .setPath("Trash-Test-path");
        Mockito.when(trashRepository.findTrashByDriveName(driveName)).thenReturn(Optional.of(trash));
        Trash trashByDriveName = trashService.getTrashByDriveName(driveName);
        assertNotNull(trashByDriveName);
        assertEquals(trash.getId(), trashByDriveName.getId());
        assertEquals(trash.getName(), trashByDriveName.getName());
        assertEquals(trash.getPath(), trashByDriveName.getPath());
    }
}