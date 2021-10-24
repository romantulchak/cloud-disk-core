package com.romantulchak.clouddisk.exception;

public class CreateFolderException extends RuntimeException {
    public CreateFolderException(){
        super("Something went wrong while creating the folder, try again");
    }
}
