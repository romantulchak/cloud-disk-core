package com.romantulchak.clouddisk.exception;

import java.util.UUID;

public class FileNotFoundException extends RuntimeException {
    public FileNotFoundException(UUID link){
        super(String.format("File with link %s not found", link));
    }
}
