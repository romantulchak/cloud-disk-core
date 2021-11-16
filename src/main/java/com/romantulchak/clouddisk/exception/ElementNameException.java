package com.romantulchak.clouddisk.exception;


public class ElementNameException extends RuntimeException {
    public ElementNameException(String name) {
        super(String.format("Element have the same name %s", name));
    }
}
