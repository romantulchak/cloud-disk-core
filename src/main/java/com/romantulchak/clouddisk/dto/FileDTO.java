package com.romantulchak.clouddisk.dto;

import com.mapperDTO.annotation.DTO;
import com.romantulchak.clouddisk.model.Store;
import com.romantulchak.clouddisk.model.enums.ContextType;

@DTO
public class FileDTO extends StoreAbstractDTO implements Store {

    private String size;

    @Override
    public ContextType getContext() {
        return ContextType.FILE;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }
}
