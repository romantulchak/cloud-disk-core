package com.romantulchak.clouddisk.utils;

import com.romantulchak.clouddisk.model.Drive;
import com.romantulchak.clouddisk.model.LocalPath;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class FolderUtils {

    private final String drivePath;

    private final String host;

    public FolderUtils(String drivePath, String host) {
        this.drivePath = drivePath;
        this.host = host;
    }

    public LocalPath createDrive(String driveName){
        try {
            String shorPath = String.join("/", drivePath, driveName);
            Path path = Paths.get(shorPath);
            Files.createDirectory(path);
            String fullPath = String.join("/", host, "cloud-disk-files", driveName);
            return new LocalPath(fullPath ,shorPath);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("See logs");
        }
    }

    public LocalPath createFolder(String driveName, String folderName){
        return null;
    }

    public LocalPath createFolder(String folderName, Drive drive){
        try {
            String shortPath = String.join("/", drive.getShortPath(), folderName) ;
            Path path = Paths.get(shortPath);
            Files.createDirectory(path);
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("See logs");
        }
    }


}
