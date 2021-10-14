package com.romantulchak.clouddisk.dto;

import com.fasterxml.jackson.annotation.JsonView;
import com.mapperDTO.annotation.DTO;
import com.mapperDTO.annotation.MapToDTO;
import com.romantulchak.clouddisk.model.Store;
import com.romantulchak.clouddisk.model.View;
import com.romantulchak.clouddisk.model.enums.ContextType;
import com.romantulchak.clouddisk.model.enums.FolderColorType;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@DTO
public class FolderDTO extends StoreAbstractDTO implements Store {

    @JsonView({View.FolderFileView.class,View.FolderView.class})
    @MapToDTO(mapClass = {View.FolderFileView.class,View.FolderView.class})
    private String color;

    @Override
    public ContextType getContext() {
        return ContextType.FOLDER;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }
}
