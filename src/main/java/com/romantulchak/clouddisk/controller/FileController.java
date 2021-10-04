package com.romantulchak.clouddisk.controller;

import com.romantulchak.clouddisk.dto.FileDTO;
import com.romantulchak.clouddisk.service.FileService;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
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
    public List<FileDTO> uploadIntoFolder(@RequestPart(value = "file") List<MultipartFile> files,
                                          @PathVariable("folderLink") UUID folderLink,
                                          Authentication authentication) throws ExecutionException, InterruptedException {
        return fileService.uploadFiles(files,folderLink, authentication).get();
    }


    @PostMapping("/upload-into-folder/{folderLink}")
    public FileDTO uploadIntoFolder(@RequestPart(value = "file") MultipartFile file,
                                          @PathVariable("folderLink") UUID folderLink,
                                          Authentication authentication) throws ExecutionException, InterruptedException {
        return fileService.uploadFiles(file,folderLink, authentication).get();
    }
}
