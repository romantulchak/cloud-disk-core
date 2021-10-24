package com.romantulchak.clouddisk.exception;

public class RemoveElementException extends RuntimeException {
    public RemoveElementException(){
        super("An error occurred while deleting the element. Please try again");
    }
}
