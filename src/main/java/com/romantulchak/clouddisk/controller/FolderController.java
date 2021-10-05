package com.romantulchak.clouddisk.controller;

import com.fasterxml.jackson.annotation.JsonView;
import com.romantulchak.clouddisk.dto.FolderDTO;
import com.romantulchak.clouddisk.model.Store;
import com.romantulchak.clouddisk.model.View;
import com.romantulchak.clouddisk.service.FolderService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@CrossOrigin(value = "*", maxAge = 3600L)
@RequestMapping("/api/folders")
public class FolderController {

    private final FolderService folderService;

    public FolderController(FolderService folderService) {
        this.folderService = folderService;
    }

    @PostMapping("/create/{driveName}")
    @PreAuthorize("hasRole('USER') AND @userDriverAccess.checkAccess(#driveName, authentication)")
    @JsonView(View.FolderView.class)
    public FolderDTO createFolder(@RequestBody String folderName, @PathVariable("driveName") String driveName, Authentication authentication){
        return folderService.create(folderName, driveName, authentication);
    }

    @GetMapping("/{driveName}")
    @PreAuthorize("hasRole('USER') AND @userDriverAccess.checkAccess(#driveName, authentication)")
    @JsonView(View.FolderFileView.class)
    public List<Store> findAllFoldersForDrive(@PathVariable("driveName") String driveName){
        return folderService.findAllFoldersForDrive(driveName);
    }
    @PostMapping("/create-subfolder/{mainFolderLink}")
    @PreAuthorize("hasRole('USER') AND @userFolderAccess.isAccessToSubFolder(#folderLink, authentication)")
    @JsonView(View.FolderView.class)
    public FolderDTO createSubFolder(@RequestBody String folderName,
                                     @PathVariable("mainFolderLink") UUID folderLink,
                                     Authentication authentication){
        return folderService.createSubFolder(folderName, folderLink, authentication);
    }

    @GetMapping("/sub-folders/{folderLink}")
    @PreAuthorize("hasRole('USER') AND @userFolderAccess.isAccessToSubFolder(#folderLink, authentication)")
    @JsonView(View.FolderFileView.class)
    public List<Store> findSubFoldersInFolder(@PathVariable("folderLink") UUID folderLink){
        return folderService.findSubFoldersInFolder(folderLink);
    }

    @DeleteMapping("/remove/{folderLink}")
    @PreAuthorize("hasRole('USER') AND @userFolderAccess.isAccessToSubFolder(#folderLink, authentication)")
    public void removeFolder(@PathVariable("folderLink") UUID folderLink){
        folderService.removeFolder(folderLink);
    }


}
