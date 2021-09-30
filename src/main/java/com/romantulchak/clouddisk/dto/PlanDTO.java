package com.romantulchak.clouddisk.dto;

import com.mapperDTO.annotation.DTO;
import com.romantulchak.clouddisk.model.enums.PlanType;

import java.util.List;

@DTO
public class PlanDTO {

    private long id;

    private PlanType name;

    private List<DriveDTO> drives;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public PlanType getName() {
        return name;
    }

    public void setName(PlanType name) {
        this.name = name;
    }

    public List<DriveDTO> getDrives() {
        return drives;
    }

    public void setDrives(List<DriveDTO> drives) {
        this.drives = drives;
    }
}
