package com.romantulchak.clouddisk.exception;

public class ElementAccessAlreadyExistsException extends RuntimeException {
    public ElementAccessAlreadyExistsException(){
        super("Access to this element already exists");
    }
}
