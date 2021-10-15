package com.romantulchak.clouddisk.dto;

import com.fasterxml.jackson.annotation.JsonView;
import com.mapperDTO.annotation.DTO;
import com.mapperDTO.annotation.MapToDTO;
import com.romantulchak.clouddisk.model.StoreAbstract;
import com.romantulchak.clouddisk.model.View;
import com.romantulchak.clouddisk.model.enums.StoreAccessType;

import javax.persistence.*;
import java.time.LocalDateTime;

@DTO
public class ElementAccessDTO {

    @MapToDTO(mapClass = {View.ElementAccessView.class})
    @JsonView(View.ElementAccessView.class)
    private long id;

    @MapToDTO(mapClass = {View.ElementAccessView.class})
    @JsonView(View.ElementAccessView.class)
    private LocalDateTime createAt;

    @MapToDTO(mapClass = {View.ElementAccessView.class})
    @JsonView(View.ElementAccessView.class)
    private StoreAccessType accessType;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public LocalDateTime getCreateAt() {
        return createAt;
    }

    public void setCreateAt(LocalDateTime createAt) {
        this.createAt = createAt;
    }

    public StoreAccessType getAccessType() {
        return accessType;
    }

    public void setAccessType(StoreAccessType accessType) {
        this.accessType = accessType;
    }
}
