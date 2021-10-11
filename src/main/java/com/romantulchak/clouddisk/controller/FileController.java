package com.romantulchak.clouddisk.controller;

import com.romantulchak.clouddisk.dto.FileDTO;
import com.romantulchak.clouddisk.service.FileService;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

@RestController
@CrossOrigin(value = "*", maxAge = 3600L)
@RequestMapping("/api/files")
public class FileController {

    private final FileService fileService;

    public FileController(FileService fileService) {
        this.fileService = fileService;
    }

    @PostMapping("/upload-into-folder/{folderLink}")
    @PreAuthorize("hasRole('USER') AND @userFolderAccess.isAccessToSubFolder(#folderLink, authentication)")
    public FileDTO uploadIntoFolder(@RequestPart(value = "file") MultipartFile file,
                                    @PathVariable("folderLink") UUID folderLink,
                                    Authentication authentication) throws ExecutionException, InterruptedException {
        return fileService.uploadFileIntoFolder(file, folderLink, authentication).get();
    }

    @PostMapping("/upload-into-drive/{driveName}")
    @PreAuthorize("hasRole('USER') AND @userDriverAccess.checkAccess(#driveName, authentication)")
    public FileDTO uploadIntoDrive(@RequestPart(value = "file") MultipartFile file,
                                   @PathVariable("driveName") String driveName,
                                   Authentication authentication) throws ExecutionException, InterruptedException {
        return fileService.uploadFileIntoDrive(file, driveName, authentication).get();
    }

    @DeleteMapping("/delete-file/{fileLink}")
    @PreAuthorize("hasRole('USER') AND @userFileAccess.hasAccess(#fileLink, authentication)")
    public void deleteFileInFolder(@PathVariable("fileLink") UUID fileLink) {
        fileService.fullDeleteFile(fileLink);
    }

    @GetMapping(value = "/download-file/{fileLink}")
    @ResponseBody
    public ResponseEntity<Resource> downloadFile(@PathVariable("fileLink") UUID link) throws IOException {
        return fileService.downloadFile(link);
    }

    @PutMapping("/pre-delete-file/{fileLink}")
    @PreAuthorize("hasRole('USER') AND @userFileAccess.hasAccess(#fileLink, authentication)")
    public void preDeleteFile(@PathVariable("fileLink") UUID fileLink, @RequestBody String driveName){
        fileService.preDeleteFile(fileLink, driveName);
    }

    @PutMapping("/restore-file/{fileLink}")
    @PreAuthorize("hasRole('USER') AND @userFileAccess.hasAccess(#fileLink, authentication)")
    public void restoreFile(@PathVariable("fileLink") UUID fileLink){
        fileService.restoreFile(fileLink);
    }
}
