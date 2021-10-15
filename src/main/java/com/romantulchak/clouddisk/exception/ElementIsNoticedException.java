package com.romantulchak.clouddisk.exception;

public class ElementIsNoticedException extends RuntimeException {
    public ElementIsNoticedException(String name) {
        super(String.format("Element %s is noticed yet", name));

    }
}
