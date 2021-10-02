package com.romantulchak.clouddisk.service.impl;

import com.mapperDTO.mapper.EntityMapperInvoker;
import com.romantulchak.clouddisk.dto.FileDTO;
import com.romantulchak.clouddisk.model.File;
import com.romantulchak.clouddisk.model.User;
import com.romantulchak.clouddisk.model.View;
import com.romantulchak.clouddisk.repository.FileRepository;
import com.romantulchak.clouddisk.service.FileService;
import com.romantulchak.clouddisk.utils.FileUtils;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class FileServiceImpl implements FileService {

    private final FileRepository fileRepository;
    private final EntityMapperInvoker<File, FileDTO> entityMapperInvoker;

    public FileServiceImpl(FileRepository fileRepository,
                           EntityMapperInvoker<File, FileDTO> entityMapperInvoker) {
        this.fileRepository = fileRepository;
        this.entityMapperInvoker = entityMapperInvoker;
    }


    @Override
    public List<FileDTO> findFilesInFolder(UUID folderLink) {
        return fileRepository.findFirstByFolderLink(folderLink)
                .stream()
                .map(file -> convertToDTO(file, View.FolderFileView.class))
                .collect(Collectors.toList());
    }

    @Override
    public List<FileDTO> findFilesInDrive(String driveName) {
        return fileRepository.findFirstByDriveName(driveName).stream()
                .map(file -> convertToDTO(file, View.FolderFileView.class))
                .collect(Collectors.toList());
    }

    @Async
    @Override
    public void uploadFiles(List<MultipartFile> multipartFiles, UUID folderLink, Authentication authentication) {
        List<File> files = new ArrayList<>();
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        for (MultipartFile file : multipartFiles) {
            File f = new File()
                    .setName(file.getOriginalFilename())
                    .setCreateAt(LocalDateTime.now())
                    .setUploadAt(LocalDateTime.now())
                    .setExtension(FileUtils.getFileExtension(file.getOriginalFilename()))
                    .setLink(UUID.randomUUID())
                    .setOwner(new User(userDetails.getId(), userDetails.getFirstName(), userDetails.getLastName()))
                    .setSize(file.getSize());

            files.add(f);
        }
        System.out.println(files);
    }

    private FileDTO convertToDTO(File file, Class<?> classToCheck){
        return entityMapperInvoker.entityToDTO(file, FileDTO.class, classToCheck);
    }
}
