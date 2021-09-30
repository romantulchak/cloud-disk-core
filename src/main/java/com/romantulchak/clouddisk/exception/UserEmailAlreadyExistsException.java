package com.romantulchak.clouddisk.exception;

public class UserEmailAlreadyExistsException extends RuntimeException{
    public UserEmailAlreadyExistsException(String email){
        super(String.format("Email %s already exists", email));
    }
}
