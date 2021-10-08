package com.romantulchak.clouddisk.utils;

import com.romantulchak.clouddisk.model.LocalPath;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.util.Comparator;
import java.util.stream.Stream;


//TODO: work with exceptions
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
            String fullPath = getFullPath(driveName);
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
            String fullPath = getFullPath(getFileRelativePath(folderPath));
            return new LocalPath(fullPath, folderPath);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("See logs");
        }
    }

    public String createTrash(String trashName, String shortPath) {
        try {
            String trashPath = String.join("/", shortPath, trashName);
            Path path = Paths.get(trashPath);
            Files.createDirectory(path);
            String fullPath = "";
            return trashPath;
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("see logs");
        }
    }

    //TODO: fix FULL PATH without user folder
    public LocalPath uploadFile(MultipartFile multipartFile, String shortPath) {
        try {
            shortPath = String.join("/", shortPath, multipartFile.getOriginalFilename());
            Path path = Paths.get(shortPath);
            String fullPath = getFullPath(getFileRelativePath(shortPath));
            multipartFile.transferTo(path);
            return new LocalPath(fullPath, shortPath);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("See logs");
        }
    }

    private String getFullPath(String fileRelativePath) {
        return String.join("/", host, FOLDER_KEY, fileRelativePath);
    }

    public boolean removeElement(String folderPath) {
        Path path = Paths.get(folderPath);
        try (Stream<Path> walk = Files.walk(path)) {
            walk.sorted(Comparator.reverseOrder())
                    .map(Path::toFile)
                    .forEach(File::delete);
            return true;
        } catch (NoSuchFileException ex) {
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("See logs");
        }
    }

    public LocalPath moveFileToTrash(String filePath, String trashPath, String filename){
        try {
            String localPath = String.join("/", trashPath, filename);
            Path shortTrashPath = Paths.get(localPath);
            Path shortFilePath = Paths.get(filePath);
            Files.move(shortFilePath, shortTrashPath, StandardCopyOption.REPLACE_EXISTING);
            return new LocalPath(getFullPath(getFileRelativePath(localPath)), localPath);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("See logs");
        }
    }

    //TODO: fix regex
    private String getFileRelativePath(String path) {
        return path.replaceAll("[A-z]:\\\\[A-z@*+-\\/*#$%^&()=\\[\\\\\\]{}\\\"\\'?]*\\/", "");
    }

}
