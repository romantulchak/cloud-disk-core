package com.romantulchak.clouddisk.utils;

import com.romantulchak.clouddisk.exception.FileNotFoundException;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class FileUtils {

    private FileUtils(){}

    public static String getParentRootPath(String shortPath){
        String rootPath = "";
        int index = shortPath.lastIndexOf("/");
        if (index > 0) {
            rootPath = shortPath.substring(0, index);
        }
        return rootPath;
    }

    public static ResponseEntity<Resource> getResource(Path path, String shortPath) throws IOException {
        Resource resource = new UrlResource(path.toUri());
        if (resource.exists() || resource.isReadable()){
            return ResponseEntity.ok().header(HttpHeaders.CONTENT_TYPE, Files.probeContentType(path))
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                    .body(resource);
        }else{
            throw new FileNotFoundException(shortPath);
        }
    }
}
