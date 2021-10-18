package com.romantulchak.clouddisk.dto;

import com.fasterxml.jackson.annotation.JsonView;
import com.mapperDTO.annotation.DTO;
import com.mapperDTO.annotation.MapToDTO;
import com.romantulchak.clouddisk.model.View;
import com.romantulchak.clouddisk.model.enums.StoreAccessType;

import java.time.LocalDateTime;

@DTO
public class ElementAccessDTO {

    private long id;

    private LocalDateTime createAt;

    @MapToDTO(mapClass = {View.ElementAccessView.class, View.FolderFileView.class})
    @JsonView({View.ElementAccessView.class, View.FolderFileView.class})
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
