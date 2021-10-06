package com.romantulchak.clouddisk.exception;

import java.util.UUID;

public class FileNotFoundException extends RuntimeException {
    public FileNotFoundException(UUID link){
        super(String.format("File with link %s not found", link));
    }

    public FileNotFoundException(String path){
        super(String.format("File with  %s not found", path));
    }


}
