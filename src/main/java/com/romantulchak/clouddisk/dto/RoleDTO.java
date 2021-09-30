package com.romantulchak.clouddisk.dto;

import com.mapperDTO.annotation.DTO;
import com.romantulchak.clouddisk.model.enums.RoleType;

@DTO
public class RoleDTO {

    private long id;

    private RoleType name;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public RoleType getName() {
        return name;
    }

    public void setName(RoleType name) {
        this.name = name;
    }
}
