package com.romantulchak.clouddisk.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreType;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonView;
import com.mapperDTO.annotation.DTO;
import com.mapperDTO.annotation.MapToDTO;
import com.romantulchak.clouddisk.model.Store;
import com.romantulchak.clouddisk.model.View;
import com.romantulchak.clouddisk.model.enums.ContextType;

@DTO
public class FileDTO extends StoreAbstractDTO implements Store {

    @MapToDTO(mapClass = {View.FolderFileView.class})
    @JsonView(View.FolderFileView.class)
    private long size;

    @Override
    public ContextType getContext() {
        return ContextType.FILE;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    @Override
    public FileDTO setNoticed(boolean noticed) {
        super.setNoticed(noticed);
        return this;
    }

    @Override
    public FileDTO setOwner(boolean owner) {
        super.setOwner(owner);
        return this;
    }
}
