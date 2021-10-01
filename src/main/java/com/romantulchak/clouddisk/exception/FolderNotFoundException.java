package com.romantulchak.clouddisk.exception;

public class FolderNotFoundException extends RuntimeException {
    public FolderNotFoundException(String folderLink) {
        super(String.format("Folder with link %s not found", folderLink));
    }
}
