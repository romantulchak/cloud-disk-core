package com.romantulchak.clouddisk.utils;

import com.romantulchak.clouddisk.exception.CreateFolderException;
import com.romantulchak.clouddisk.exception.FileAlreadyMovedException;
import com.romantulchak.clouddisk.exception.FileWithNameAlreadyExistsException;
import com.romantulchak.clouddisk.exception.RemoveElementException;
import com.romantulchak.clouddisk.model.LocalPath;
import org.apache.commons.lang3.SystemUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.util.Comparator;
import java.util.stream.Stream;


@Component
public class FolderUtils {

    private static final Logger LOGGER = LoggerFactory.getLogger(FolderUtils.class);

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
        }catch (FileAlreadyExistsException ex){
            LOGGER.error(ex.getMessage());
            throw new FileWithNameAlreadyExistsException(driveName);
        }catch (IOException ex){
            LOGGER.error(ex.getMessage());
            throw new CreateFolderException();
        }
    }


    public LocalPath createFolder(String folderName, String shortPath) {
        try {
            String folderPath = String.join("/", shortPath, folderName);
            Path path = Paths.get(folderPath);
            Files.createDirectory(path);
            String fullPath = getFullPath(getFileRelativePath(folderPath));
            return new LocalPath(fullPath, folderPath);
        }catch (FileAlreadyExistsException ex){
            LOGGER.error(ex.getMessage());
            throw new FileWithNameAlreadyExistsException(folderName);
        }catch (IOException ex){
            LOGGER.error(ex.getMessage());
            throw new CreateFolderException();
        }
    }

    public String createTrash(String trashName, String shortPath) {
        try {
            String trashPath = String.join("/", shortPath, trashName);
            Path path = Paths.get(trashPath);
            Files.createDirectory(path);
            return trashPath;
        }catch (FileAlreadyExistsException ex){
            LOGGER.error(ex.getMessage());
            throw new FileWithNameAlreadyExistsException(trashName);
        }catch (IOException ex){
            LOGGER.error(ex.getMessage());
            throw new CreateFolderException();
        }
    }

    public LocalPath uploadFile(MultipartFile multipartFile, String shortPath) {
        try {
            shortPath = String.join("/", shortPath, multipartFile.getOriginalFilename());
            Path path = Paths.get(shortPath);
            String fullPath = getFullPath(getFileRelativePath(shortPath));
            multipartFile.transferTo(path);
            return new LocalPath(fullPath, shortPath);
        }catch (IllegalStateException | IOException e){
            LOGGER.error(e.getMessage());
            throw new FileAlreadyMovedException();
        }
    }


    public boolean removeElement(String folderPath) {
        Path path = Paths.get(folderPath);
        try (Stream<Path> walk = Files.walk(path)) {
            walk.sorted(Comparator.reverseOrder())
                    .map(Path::toFile)
                    .forEach(File::deleteOnExit);
            throw new NoSuchFileException(folderPath);
        } catch (NoSuchFileException ex) {
            return true;
        } catch (IOException e) {
            LOGGER.error(e.getMessage());
            throw new RemoveElementException();
        }
    }

    public LocalPath moveFileToTrash(String filePath, String trashPath, String filename){
        try {
            String localPath = String.join("/", trashPath, filename);
            Path shortTrashPath = Paths.get(localPath);
            Path shortFilePath = Paths.get(filePath);
            Files.move(shortFilePath, shortTrashPath, StandardCopyOption.REPLACE_EXISTING);
            return new LocalPath(getFullPath(getFileRelativePath(localPath)), localPath);
        }catch (IllegalStateException | IOException e){
            LOGGER.error(e.getMessage());
            throw new FileAlreadyMovedException();
        }
    }

    public boolean restoreFile(String oldPath, String newPath) {
        try {
            Path oldShortPath = Paths.get(oldPath);
            Path newShortPath = Paths.get(newPath);
            Files.move(oldShortPath, newShortPath);
            return true;
        }catch (IllegalStateException | IOException e){
            LOGGER.error(e.getMessage());
            throw new FileAlreadyMovedException();
        }
    }

    //TODO: fix regex
    private String getFileRelativePath(String path) {
        String osName = SystemUtils.OS_NAME;
        if (osName.contains("Windows")){
            return path.replaceAll("[A-z]:\\\\[A-z@*+-\\/*#$%^&()=\\[\\\\\\]{}\\\"\\'?]*\\/", "");
        }
        return path.replace("home\\/cloud-disk-files\\/", "");
    }

    private String getFullPath(String fileRelativePath) {
        return String.join("/", host, FOLDER_KEY, fileRelativePath);
    }

}
