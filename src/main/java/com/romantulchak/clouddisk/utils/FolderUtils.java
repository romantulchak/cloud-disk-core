package com.romantulchak.clouddisk.utils;

import com.romantulchak.clouddisk.model.LocalPath;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;


//TODO: fix files
@Component
public class FolderUtils {

    private final String drivePath;

    private final String host;

    private static final String FOLDER_KEY = "cloud-disk-files";

    public FolderUtils(@Value("${cloud.disk.files.folder}") String drivePath, @Value("${cloud.disk.host}") String host) {
        this.drivePath = drivePath;
        this.host = host;
    }

    public LocalPath createDrive(String driveName) {
        try {
            String shorPath = String.join("/", drivePath, driveName);
            Path path = Paths.get(shorPath);
            Files.createDirectory(path);
            String fullPath = String.join("/", host, FOLDER_KEY, driveName);
            return new LocalPath(fullPath, shorPath);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("See logs");
        }
    }


    public LocalPath createFolder(String folderName, String shortPath) {
        try {
            String folderPath = String.join("/", shortPath, folderName);
              Path path = Paths.get(folderPath);
            Files.createDirectory(path);
            String fullPath = String.join("/", host, FOLDER_KEY, getFileRelativePath(folderPath));
            return new LocalPath(fullPath, folderPath);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("See logs");
        }
    }

    public LocalPath uploadFile(MultipartFile multipartFile, String shortPath) {
        try {
            shortPath = String.join("/", shortPath, multipartFile.getOriginalFilename());
            Path path = Paths.get(shortPath);
            String fullPath = String.join("/", host, FOLDER_KEY, getFileRelativePath(shortPath));
            multipartFile.transferTo(path);
            return new LocalPath(fullPath, shortPath);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("See logs");
        }

    }

    private String getFileRelativePath(String path){
        return path.replaceAll("[A-z]*:\\\\[a-zA-Z0-9!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>/?]*(?=[a-zA-Z-])[0-9a-fA-F]{8}[0-9a-fA-F]{4}[0-9a-fA-F]{4}[0-9a-fA-F]{4}[0-9a-fA-F]{12}/", "");

    }

}
