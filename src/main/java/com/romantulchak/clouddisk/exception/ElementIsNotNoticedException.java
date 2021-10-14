package com.romantulchak.clouddisk.exception;

public class ElementIsNotNoticedException extends RuntimeException {
    public ElementIsNotNoticedException(String name) {
        super(String.format("Element %s is not noticed yet", name));

    }
}
