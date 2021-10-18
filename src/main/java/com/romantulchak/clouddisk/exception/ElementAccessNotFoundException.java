package com.romantulchak.clouddisk.exception;

import java.util.UUID;

public class ElementAccessNotFoundException extends RuntimeException {
    public ElementAccessNotFoundException(UUID link) {
        super(String.format("Access to element %s not found", link.toString()));
    }
}
