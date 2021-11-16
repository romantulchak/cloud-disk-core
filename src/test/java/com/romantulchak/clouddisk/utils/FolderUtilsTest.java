package com.romantulchak.clouddisk.utils;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

@ExtendWith(MockitoExtension.class)
class FolderUtilsTest {

    @InjectMocks
    private FolderUtils folderUtils;


    @BeforeEach
    public void setUp(){
        ReflectionTestUtils.setField(folderUtils, "drivePath", "E:\\cloud-disk-files");
    }


    @Test
    void createDrive() {

        folderUtils.createDrive("Test");
    }

    @Test
    void createFolder() {
    }

    @Test
    void createTrash() {
    }

    @Test
    void uploadFile() {
    }

    @Test
    void removeElement() {
    }

    @Test
    void moveFileToTrash() {
    }

    @Test
    void restoreElement() {
    }
}