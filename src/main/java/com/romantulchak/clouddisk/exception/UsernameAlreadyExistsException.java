package com.romantulchak.clouddisk.exception;

public class UsernameAlreadyExistsException extends RuntimeException{
    public UsernameAlreadyExistsException(String username){
        super(String.format("Username %s already taken", username));
    }
}
