package com.romantulchak.clouddisk.controller;

import com.fasterxml.jackson.annotation.JsonView;
import com.romantulchak.clouddisk.dto.FolderDTO;
import com.romantulchak.clouddisk.model.Store;
import com.romantulchak.clouddisk.model.View;
import com.romantulchak.clouddisk.service.FolderService;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

@RestController
@CrossOrigin(value = "*", maxAge = 3600L)
@RequestMapping("/api/folder")
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

    @PostMapping("/create-subfolder/{mainFolderLink}")
    @PreAuthorize("hasRole('USER') AND @userElementAccess.hasEditAccess(#folderLink, authentication)")
    @JsonView(View.FolderView.class)
    public FolderDTO createSubFolder(@RequestBody String folderName,
                                     @PathVariable("mainFolderLink") UUID folderLink,
                                     Authentication authentication){
        return folderService.createSubFolder(folderName, folderLink, authentication);
    }

    @GetMapping("/sub-folders/{folderLink}")
    @PreAuthorize("hasRole('USER') AND @userFolderAccess.isAccessToSubFolder(#folderLink, authentication)")
    @JsonView(View.FolderFileView.class)
    public List<Store> findSubFoldersInFolder(@PathVariable("folderLink") UUID folderLink, @RequestParam("page") String page){
        return folderService.findSubFoldersInFolder(folderLink, page);
    }

    @GetMapping("/download-folder/{folderLink}")
    @ResponseBody
    public ResponseEntity<Resource> downloadFolder(@PathVariable("folderLink") UUID folderLink) throws IOException {
        return folderService.downloadFolder(folderLink);
    }

    @PutMapping("/change-color/{folderLink}")
    @PreAuthorize("hasRole('USER') AND @userFolderAccess.isAccessToSubFolder(#folderLink, authentication)")
    @JsonView(View.FolderFileView.class)
    public FolderDTO changeColor(@PathVariable("folderLink") UUID folderLink, @RequestBody String color){
        return folderService.changeColor(folderLink, color);
    }

    @PostMapping(value = "/upload-in-drive")
    @PreAuthorize("hasRole('USER') AND @userDriverAccess.checkAccess(#driveName, authentication)")
    @JsonView(View.FolderFileView.class)
    public FolderDTO uploadInDrive(@RequestPart(value = "files") List<MultipartFile> files, @RequestPart(value = "driveName") String driveName) throws ExecutionException, InterruptedException {
        return folderService.uploadInDrive(files, driveName).get();
    }

    @PostMapping(value = "/upload")
    @PreAuthorize("hasRole('USER') AND @userElementAccess.hasFullAccess(#link, authentication)")
    @JsonView(View.FolderFileView.class)
    public FolderDTO uploadIntoFolder(@RequestPart(value = "files") List<MultipartFile> files, @RequestPart(value = "folderLink") String link) throws ExecutionException, InterruptedException {
        return folderService.uploadInFolder(files, link).get();
    }


}
