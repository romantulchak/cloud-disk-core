package com.romantulchak.clouddisk.exception;

public class DriveNotFoundException extends RuntimeException {
    public DriveNotFoundException(String username) {
        super(String.format("Drive for user %s not found", username));
    }
}
