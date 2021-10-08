package com.romantulchak.clouddisk.exception;

public class ObjectAlreadyPreRemovedException extends RuntimeException {

    public ObjectAlreadyPreRemovedException(String name) {
        super(String.format("Object %s already pre-removed", name));
    }
}
