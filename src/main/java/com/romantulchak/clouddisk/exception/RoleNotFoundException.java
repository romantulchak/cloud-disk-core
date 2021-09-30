package com.romantulchak.clouddisk.exception;

import com.romantulchak.clouddisk.model.enums.RoleType;

public class RoleNotFoundException extends RuntimeException {
    public RoleNotFoundException(RoleType roleUser) {
        super(String.format("Role %s not found", roleUser.name()));
    }
}
