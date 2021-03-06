package com.romantulchak.clouddisk.dto;

import com.fasterxml.jackson.annotation.JsonView;
import com.mapperDTO.annotation.DTO;
import com.mapperDTO.annotation.MapToDTO;
import com.romantulchak.clouddisk.model.Store;
import com.romantulchak.clouddisk.model.View;
import com.romantulchak.clouddisk.model.enums.ContextType;

@DTO
public class FolderDTO extends StoreAbstractDTO implements Store {

    @JsonView({View.FolderFileView.class, View.FolderView.class})
    @MapToDTO(mapClass = {View.FolderFileView.class, View.FolderView.class})
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

    @Override
    public FolderDTO setNoticed(boolean noticed) {
        super.setNoticed(noticed);
        return this;
    }

    @Override
    public FolderDTO setOwner(boolean owner) {
        super.setOwner(owner);
        return this;
    }

}
