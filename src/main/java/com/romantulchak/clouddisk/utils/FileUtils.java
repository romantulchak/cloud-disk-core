package com.romantulchak.clouddisk.utils;

import com.romantulchak.clouddisk.constant.ApplicationConstant;
import com.romantulchak.clouddisk.exception.FileNotFoundException;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Base64;

public class FileUtils {

    private FileUtils() {
    }

    public static String getParentRootPath(String shortPath) {
        String rootPath = "";
        int index = shortPath.lastIndexOf(ApplicationConstant.SLASH);
        if (index > 0) {
            rootPath = shortPath.substring(0, index);
        }
        return rootPath;
    }

    /**
     * Gets Resource from path for downloading
     *
     * @param path path to file
     * @return Resource from the file to download
     * @throws IOException if file not found
     */
    public static ResponseEntity<Resource> getResource(Path path) throws IOException {
        Resource resource = new UrlResource(path.toUri());
        if (resource.exists() || resource.isReadable()) {
            return ResponseEntity.ok().header(HttpHeaders.CONTENT_TYPE, Files.probeContentType(path))
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                    .body(resource);
        } else {
            throw new FileNotFoundException(path.getFileName().toString());
        }
    }

    /**
     * Gets main folder name from path
     * Ex: /home/var/test/test.txt return home
     *
     * @param path to file
     * @return main folder name from string
     */
    public static String getMainFolderName(String path) {
        int indexOfLastSlash = path.lastIndexOf(ApplicationConstant.SLASH);
        if (indexOfLastSlash != -1) {
            String[] fileNameParts = path.substring(0, indexOfLastSlash).split(ApplicationConstant.SLASH);
            if (fileNameParts.length >= 3) {
                path = path.substring(0, indexOfLastSlash);
                return path.substring(0, path.lastIndexOf(ApplicationConstant.SLASH));
            } else if (fileNameParts.length == 2) {
                return fileNameParts[0];
            }
        }
        return path;
    }

    /**
     * Gets parent folder path from path
     * Ex: /home/var/test/test.txt return /home/var/test
     *
     * @param path to file
     * @return parent folder path
     */
    public static String getParentFolderPath(String path) {
        int indexOfLastSlash = path.lastIndexOf(ApplicationConstant.SLASH);
        if (indexOfLastSlash != -1) {
            String[] fileNameParts = path.substring(0, indexOfLastSlash).split(ApplicationConstant.SLASH);
            if (fileNameParts.length >= 3) {
                return fileNameParts[fileNameParts.length - 2];
            } else if (fileNameParts.length == 2) {
                return fileNameParts[0];
            }
        }
        return path;
    }

    /**
     * Gets folder path without filename
     *
     * @param path which contains filename
     * @return path to folder without filename
     */
    public static String getFolderPath(String path) {
        int indexOfLastSlash = path.lastIndexOf(ApplicationConstant.SLASH);
        if (indexOfLastSlash != -1) {
            return path.substring(0, indexOfLastSlash);
        }
        return path;
    }

    /**
     * Gets file extension
     *
     * @param filename name of file
     * @return extension of file
     */
    public static String getFileExtension(String filename) {
        String extension = StringUtils.getFilenameExtension(filename);
        if (extension == null) {
            return "";
        }
        return ApplicationConstant.DOT + extension;
    }

    /**
     * Gets filename from path
     * Ex: /home/var/test/test.txt return test.txt
     *
     * @param name to get original filename
     * @return filename from path
     */
    public static String getName(String name) {
        int indexOfLastSlash = name.lastIndexOf(ApplicationConstant.SLASH);
        if (indexOfLastSlash != -1) {
            name = name.substring(indexOfLastSlash + 1);
        }
        return name;
    }

    /**
     * Encodes file/folder name to Base64
     *
     * @param name of file/folder
     * @return encoded file/folder name
     */
    public static String encodeElementName(String name) {
        name = FileUtils.getName(name);
        return Base64.getEncoder().encodeToString(name.getBytes(StandardCharsets.UTF_8)).replace("/", "")
                + ApplicationConstant.FILE_NAME_SEPARATOR;
    }

    /**
     * Decodes file/folder name to original name
     *
     * @param encodedName of file/folder
     * @return original name of encoded string
     */
    public static String decodeElementName(String encodedName) {
        int indexOfSeparator = encodedName.indexOf(ApplicationConstant.FILE_NAME_SEPARATOR);
        encodedName = encodedName.substring(0, indexOfSeparator);
        byte[] decode = Base64.getDecoder().decode(encodedName);
        return new String(decode);
    }
}
