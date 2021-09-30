package com.romantulchak.clouddisk.controller;

import com.fasterxml.jackson.annotation.JsonView;
import com.romantulchak.clouddisk.dto.FolderDTO;
import com.romantulchak.clouddisk.model.View;
import com.romantulchak.clouddisk.service.FolderService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(value = "*", maxAge = 3600L)
@RequestMapping("/api/folders")
public class FolderController {

    private final FolderService folderService;

    public FolderController(FolderService folderService) {
        this.folderService = folderService;
    }

    @PostMapping("/create/{driveName}")
    @PreAuthorize("hasRole('USER') AND @userDriverAccess.checkAccess(authentication, #driveName)")
    public void createFolder(@RequestBody String folderName, @PathVariable("driveName") String driveName){
        folderService.create(folderName, driveName);
    }

    @GetMapping("/{driveName}")
    @PreAuthorize("hasRole('USER') AND @userDriverAccess.checkAccess(authentication, #driveName)")
    @JsonView(View.FolderView.class)
    public List<FolderDTO> findAllFoldersForDrive(@PathVariable("driveName") String driveName){
        return folderService.findAllFoldersForDrive(driveName);
    }

}
