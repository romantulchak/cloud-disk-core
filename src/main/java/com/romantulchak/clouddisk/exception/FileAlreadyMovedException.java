package com.romantulchak.clouddisk.exception;

public class FileAlreadyMovedException extends RuntimeException {

    public FileAlreadyMovedException(String file) {
        super(String.format("File %s has already been moved", file));
    }
}
