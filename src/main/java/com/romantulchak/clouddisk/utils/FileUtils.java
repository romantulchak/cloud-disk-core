package com.romantulchak.clouddisk.utils;

import com.romantulchak.clouddisk.constant.FilenameConstant;
import com.romantulchak.clouddisk.exception.FileNotFoundException;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Base64;
import java.util.Objects;

public class FileUtils {

    private FileUtils() {
    }

    public static String getParentRootPath(String shortPath) {
        String rootPath = "";
        int index = shortPath.lastIndexOf(FilenameConstant.SLASH);
        if (index > 0) {
            rootPath = shortPath.substring(0, index);
        }
        return rootPath;
    }

    public static ResponseEntity<Resource> getResource(Path path, String shortPath) throws IOException {
        Resource resource = new UrlResource(path.toUri());
        if (resource.exists() || resource.isReadable()) {
            return ResponseEntity.ok().header(HttpHeaders.CONTENT_TYPE, Files.probeContentType(path))
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                    .body(resource);
        } else {
            throw new FileNotFoundException(shortPath);
        }
    }

    public static String getMainFolderName(String name) {
        int indexOfLastSlash = name.lastIndexOf(FilenameConstant.SLASH);
        if (indexOfLastSlash != -1) {
            String[] fileNameParts = name.substring(0, indexOfLastSlash).split(FilenameConstant.SLASH);
            if (fileNameParts.length >= 3) {
                name = name.substring(0, indexOfLastSlash);
                return name.substring(0, name.lastIndexOf(FilenameConstant.SLASH));
            } else if (fileNameParts.length == 2) {
                return fileNameParts[0];
            }
        }
        return name;
    }

    public static String getParentFolderName(String name) {
        int indexOfLastSlash = name.lastIndexOf(FilenameConstant.SLASH);
        if (indexOfLastSlash != -1) {
            String[] fileNameParts = name.substring(0, indexOfLastSlash).split(FilenameConstant.SLASH);
            if (fileNameParts.length >= 3) {
                return fileNameParts[fileNameParts.length - 2];
            } else if (fileNameParts.length == 2) {
                return fileNameParts[0];
            }
        }
        return name;
    }

    public static String getFolderPath(String name) {
        int indexOfLastSlash = name.lastIndexOf(FilenameConstant.SLASH);
        if (indexOfLastSlash != -1) {
            return name.substring(0, indexOfLastSlash);
        }
        return name;
    }

    public static String getFileName(MultipartFile file) {
        String fileName = Objects.requireNonNull(file.getOriginalFilename());
        int indexOfLastSlash = fileName.lastIndexOf(FilenameConstant.SLASH);
        return Objects.requireNonNull(file.getOriginalFilename()).substring(indexOfLastSlash + 1);
    }

    public static String getFileExtension(String name) {
        String extension = StringUtils.getFilenameExtension(name);
        if (extension == null) {
            return "";
        }
        return FilenameConstant.DOT + extension;
    }

    public static String getName(String name) {
        int indexOfLastSlash = name.lastIndexOf(FilenameConstant.SLASH);
        if (indexOfLastSlash != -1) {
            name = name.substring(indexOfLastSlash + 1);
        }
        return name;
    }

    public static String encodeElementName(String name) {
        name = FileUtils.getName(name);
        return Base64.getEncoder().encodeToString(name.getBytes(StandardCharsets.UTF_8)) + FilenameConstant.FILE_NAME_SEPARATOR;
    }

    public static String decodeElementName(String encodedName) {
        int indexOfSeparator = encodedName.indexOf(FilenameConstant.FILE_NAME_SEPARATOR);
        encodedName = encodedName.substring(0, indexOfSeparator);
        byte[] decode = Base64.getDecoder().decode(encodedName);
        return new String(decode);
    }
}
