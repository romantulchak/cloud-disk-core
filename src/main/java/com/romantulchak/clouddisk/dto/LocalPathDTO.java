package com.romantulchak.clouddisk.dto;

import com.fasterxml.jackson.annotation.JsonView;
import com.mapperDTO.annotation.DTO;
import com.mapperDTO.annotation.MapToDTO;
import com.romantulchak.clouddisk.model.View;

@DTO
public class LocalPathDTO {

    @MapToDTO(mapClass = {View.FolderFileView.class, View.HistoryView.class})
    @JsonView({View.FolderFileView.class, View.HistoryView.class})
    private String fullPath;

    public String getFullPath() {
        return fullPath;
    }

    public LocalPathDTO setFullPath(String fullPath) {
        this.fullPath = fullPath;
        return this;
    }
}
