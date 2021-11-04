package com.romantulchak.clouddisk.utils;

import com.romantulchak.clouddisk.constant.FilenameConstant;
import com.romantulchak.clouddisk.exception.*;
import com.romantulchak.clouddisk.model.LocalPath;
import org.apache.commons.lang3.SystemUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.Base64;
import java.util.Comparator;
import java.util.UUID;
import java.util.stream.Stream;

import static com.romantulchak.clouddisk.utils.FileUtils.*;


@Component
public class FolderUtils {

    private static final Logger LOGGER = LoggerFactory.getLogger(FolderUtils.class);

    private static final String LAST_SLASH_REGEX = "\\/$";

    private final String drivePath;

    private final String host;

    private static final String FOLDER_KEY = "cloud-disk-files";

    public FolderUtils(@Value("${cloud.disk.files.folder}") String drivePath, @Value("${cloud.disk.host}") String host) {
        this.drivePath = drivePath;
        this.host = host;
    }

    public LocalPath createDrive(String driveName) {
        try {
            String shorPath = String.join(FilenameConstant.SLASH, drivePath, driveName);
            Path path = Paths.get(shorPath);
            Files.createDirectory(path);
            String fullPath = getFullPath(driveName);
            return new LocalPath(fullPath, shorPath);
        } catch (FileAlreadyExistsException ex) {
            LOGGER.error(ex.getMessage());
            throw new FileWithNameAlreadyExistsException(driveName);
        } catch (IOException ex) {
            LOGGER.error(ex.getMessage());
            throw new CreateFolderException();
        }
    }


    public LocalPath createFolder(String shortPath, String realFolderName) {
        String folderName = FileUtils.encodeElementName(realFolderName) + UUID.randomUUID();
        try {
            String folderPath = String.join(FilenameConstant.SLASH, shortPath, folderName);
            Path path = Paths.get(folderPath);
            boolean exists = Files.exists(path);
            if (!exists) {
                Files.createDirectory(path);
            }
            String fullPath = getFullPath(getFileRelativePath(folderPath));
            return new LocalPath(fullPath, folderPath);
        } catch (FileAlreadyExistsException ex) {
            LOGGER.error(ex.getMessage());
            throw new FileWithNameAlreadyExistsException(folderName);
        } catch (IOException ex) {
            LOGGER.error(ex.getMessage());
            throw new CreateFolderException();
        }
    }

    public String createTrash(String trashName, String shortPath) {
        try {
            String trashPath = String.join(FilenameConstant.SLASH, shortPath, trashName);
            Path path = Paths.get(trashPath);
            Files.createDirectory(path);
            return trashPath;
        } catch (FileAlreadyExistsException ex) {
            LOGGER.error(ex.getMessage());
            throw new FileWithNameAlreadyExistsException(trashName);
        } catch (IOException ex) {
            LOGGER.error(ex.getMessage());
            throw new CreateFolderException();
        }
    }

    public LocalPath uploadFile(MultipartFile file, String shortPath) {
        try {
            shortPath = shortPath.replaceAll(LAST_SLASH_REGEX, "");
            String fileName = encodeElementName(file.getOriginalFilename()) + UUID.randomUUID() + getFileExtension(file.getOriginalFilename());
            shortPath = String.join(FilenameConstant.SLASH, shortPath, fileName);
            Path path = Paths.get(shortPath);
            String fullPath = getFullPath(getFileRelativePath(shortPath));
            file.transferTo(path);
            return new LocalPath(fullPath, shortPath);
        } catch (FileSystemException e) {
            throw new FileUploadException(e.getReason(), file.getOriginalFilename());
        } catch (IllegalStateException | IOException e) {
            LOGGER.error(e.getMessage());
            throw new FileAlreadyMovedException(file.getOriginalFilename());
        }
    }


    public boolean removeElement(String folderPath) {
        Path path = Paths.get(folderPath);
        try (Stream<Path> walk = Files.walk(path)) {
            walk.sorted(Comparator.reverseOrder())
                    .map(Path::toFile)
                    .forEach(File::delete);
            throw new NoSuchFileException(folderPath);
        } catch (NoSuchFileException ex) {
            return true;
        } catch (IOException e) {
            LOGGER.error(e.getMessage());
            throw new RemoveElementException();
        }
    }

    public LocalPath moveFileToTrash(String filePath, String trashPath, String filename) {
        try {
            String localPath = String.join(FilenameConstant.SLASH, trashPath, filename);
            Path shortTrashPath = Paths.get(localPath);
            Path shortFilePath = Paths.get(filePath);
            Files.move(shortFilePath, shortTrashPath, StandardCopyOption.REPLACE_EXISTING);
            return new LocalPath(getFullPath(getFileRelativePath(localPath)), localPath);
        } catch (IllegalStateException | IOException e) {
            LOGGER.error(e.getMessage());
            throw new FileAlreadyMovedException(filename);
        }
    }

    public boolean restoreElement(String oldPath, String newPath) {
        try {
            Path oldShortPath = Paths.get(oldPath);
            Path newShortPath = Paths.get(newPath);
            Files.move(oldShortPath, newShortPath);
            return true;
        } catch (IllegalStateException | IOException e) {
            LOGGER.error(e.getMessage());
            throw new FileAlreadyMovedException(oldPath);
        }
    }

    //TODO: fix regex
    private String getFileRelativePath(String path) {
        String osName = SystemUtils.OS_NAME;
        if (osName.contains("Windows")) {
            return path.replaceAll("[A-z]:\\\\[A-z@*+-\\/*#$%^&()=\\[\\\\\\]{}\\\"\\'?]*\\/", "");
        }
        return path.replace("home\\/cloud-disk-files\\/", "");
    }

    private String getFullPath(String fileRelativePath) {
        return String.join("/", host, FOLDER_KEY, fileRelativePath);
    }

}
