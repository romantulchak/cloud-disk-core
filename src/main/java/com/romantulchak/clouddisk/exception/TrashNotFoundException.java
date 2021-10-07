package com.romantulchak.clouddisk.exception;

public class TrashNotFoundException extends RuntimeException {
    public TrashNotFoundException(String driveName) {
        super(String.format("Trash not found for drive: %s", driveName));
    }
}
