package com.romantulchak.clouddisk.service;

import com.romantulchak.clouddisk.dto.FolderDTO;

import java.util.List;

public interface FolderService {

    void create(String folderName, String driveName);

    List<FolderDTO> findAllFoldersForDrive(String drive);
}
