package com.romantulchak.clouddisk.exception;

import java.util.UUID;

public class FolderNotFoundException extends RuntimeException {
    public FolderNotFoundException(UUID folderLink) {
        super(String.format("Folder with link %s not found", folderLink.toString()));
    }
}
