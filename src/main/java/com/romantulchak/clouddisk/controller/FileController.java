package com.romantulchak.clouddisk.controller;

import com.romantulchak.clouddisk.service.FileService;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@RestController
@CrossOrigin(value = "*", maxAge = 3600L)
@RequestMapping("/api/files")
public class FileController {

    private final FileService fileService;

    public FileController(FileService fileService) {
        this.fileService = fileService;
    }

    @PostMapping("/upload-subfolder/{folderLink}")
    public void uploadFilesSubFolder(@RequestPart(value = "file") List<MultipartFile> files,
                                     @PathVariable("folderLink") UUID folderLink,
                                     Authentication authentication){
        fileService.uploadFiles(files,folderLink, authentication);

    }
}
