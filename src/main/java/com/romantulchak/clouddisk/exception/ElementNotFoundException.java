package com.romantulchak.clouddisk.exception;

import java.util.UUID;

public class ElementNotFoundException extends RuntimeException{
    public ElementNotFoundException(UUID link){
        super(String.format("Element with link %s not found", link.toString()));
    }
}
