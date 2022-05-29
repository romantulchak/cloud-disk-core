package com.romantulchak.clouddisk.utils;

import com.romantulchak.clouddisk.constant.ApplicationConstant;
import com.romantulchak.clouddisk.exception.RemoveElementException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;
import java.util.stream.Stream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public final class ZipUtils {

    private static final Logger LOGGER = LoggerFactory.getLogger(ZipUtils.class);


    private ZipUtils() {

    }

    //TODO: remove zip after creation / change names of files
    public static Path createZip(String elementName, String shortPath) throws IOException {
        String parentRootPath = FileUtils.getParentRootPath(shortPath);
        String zipPath = String.join(ApplicationConstant.SLASH, parentRootPath, elementName + UUID.randomUUID());
        Path path = Files.createFile(Paths.get(zipPath + ".zip"));
        try (ZipOutputStream zs = new ZipOutputStream(Files.newOutputStream(path))) {
            Path sourcePath = Paths.get(shortPath);
            try (Stream<Path> stream = Files.walk(sourcePath)) {
                stream.skip(1).forEach(p -> {
                    if (p.toFile().isDirectory()) {
                        addFolderToZip(zs, p);
                    } else {
                        addFileToZip(zs, p);
                    }
                });
            }
        }
        return path;
    }

    public static void removeCreatedZip(Path path) {
        try {
            Files.deleteIfExists(path);
        } catch (IOException e) {
            LOGGER.error(e.getMessage());
            throw new RemoveElementException();
        }
    }

    private static void addFileToZip(ZipOutputStream zs, Path p) {
        ZipEntry zipEntry = new ZipEntry(FileUtils.decodeElementName(p.toFile().getName()));
        try {
            zs.putNextEntry(zipEntry);
            Files.copy(p, zs);
            zs.closeEntry();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void addFolderToZip(ZipOutputStream zs, Path p) {
        ZipEntry zipEntry = new ZipEntry(FileUtils.decodeElementName(p.toFile().getName()) + ApplicationConstant.SLASH);
        try {
            zs.putNextEntry(zipEntry);
            zs.closeEntry();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
