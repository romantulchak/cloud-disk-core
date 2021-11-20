package com.romantulchak.clouddisk.dto;

import com.fasterxml.jackson.annotation.JsonView;
import com.mapperDTO.annotation.DTO;
import com.mapperDTO.annotation.MapToDTO;
import com.romantulchak.clouddisk.model.View;

@DTO
public class RenameHistoryDTO extends HistoryDTO{

    @MapToDTO(mapClass = {View.HistoryView.class})
    @JsonView(View.HistoryView.class)
    private String name;

    @MapToDTO(mapClass = {View.HistoryView.class})
    @JsonView(View.HistoryView.class)
    private String oldName;


    public String getName() {
        return name;
    }

    public RenameHistoryDTO setName(String name) {
        this.name = name;
        return this;
    }

    public String getOldName() {
        return oldName;
    }

    public RenameHistoryDTO setOldName(String oldName) {
        this.oldName = oldName;
        return this;
    }
}
