package com.romantulchak.clouddisk.exception;

public class FileUploadException extends RuntimeException {
    public FileUploadException(String reason, String fileName){
        super(String.format("%s %s", reason, fileName));
    }
}
