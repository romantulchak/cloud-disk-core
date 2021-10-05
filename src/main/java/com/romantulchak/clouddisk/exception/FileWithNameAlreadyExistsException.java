package com.romantulchak.clouddisk.exception;

public class FileWithNameAlreadyExistsException extends RuntimeException {
    public FileWithNameAlreadyExistsException(String fileName) {
        super(String.format("File with name %s already exists", fileName));
    }
}
