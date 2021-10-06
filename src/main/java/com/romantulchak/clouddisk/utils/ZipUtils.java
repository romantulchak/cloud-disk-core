package com.romantulchak.clouddisk.utils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;
import java.util.stream.Stream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class ZipUtils {

    public static Path createZip(String elementName, String shortPath) throws IOException {
        String parentRootPath = FileUtils.getParentRootPath(shortPath);
        String zipPath = String.join("/", parentRootPath, elementName + UUID.randomUUID());
        Path path = Files.createFile(Paths.get(zipPath + ".zip"));
        try (ZipOutputStream zs = new ZipOutputStream(Files.newOutputStream(path))) {
            Path sourcePath = Paths.get(shortPath);
            try (Stream<Path> stream = Files.walk(sourcePath)) {
                stream.skip(1).forEach(p -> {
                    if (p.toFile().isDirectory()) {
                        addFolderToZip(zs, p);
                    } else {
                        addFileToZip(zs, sourcePath, p);
                    }
                });
            }
        }
        return path;
    }

    public static void removeCreatedZip(Path path){
        try {
            Files.delete(path);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void addFileToZip(ZipOutputStream zs, Path sourcePath, Path p) {
        ZipEntry zipEntry = new ZipEntry(sourcePath.relativize(p).toString());
        try {
            zs.putNextEntry(zipEntry);
            Files.copy(p, zs);
            zs.closeEntry();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void addFolderToZip(ZipOutputStream zs, Path p) {
        ZipEntry zipEntry = new ZipEntry(p.toFile().getName() + "/");
        try {
            zs.putNextEntry(zipEntry);
            zs.closeEntry();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}