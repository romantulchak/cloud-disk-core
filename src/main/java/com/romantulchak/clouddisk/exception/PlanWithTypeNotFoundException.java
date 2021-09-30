package com.romantulchak.clouddisk.exception;

import com.romantulchak.clouddisk.model.enums.PlanType;

public class PlanWithTypeNotFoundException extends RuntimeException {
    public PlanWithTypeNotFoundException(PlanType type) {
        super(String.format("Plan type %s not found", type.name()));
    }
}
