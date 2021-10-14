package com.romantulchak.clouddisk.dto;

import com.mapperDTO.annotation.DTO;
import com.romantulchak.clouddisk.model.Store;
import com.romantulchak.clouddisk.model.enums.ContextType;

@DTO
public class FileDTO extends StoreAbstractDTO implements Store {

    @Override
    public ContextType getContext() {
        return ContextType.FILE;
    }
}
