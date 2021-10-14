package com.romantulchak.clouddisk.exception;

public class ElementAlreadyUnNoticedException extends RuntimeException {
    public ElementAlreadyUnNoticedException(String name) {
        super(String.format("Element with link %s already unnoticed", name));
    }
}
