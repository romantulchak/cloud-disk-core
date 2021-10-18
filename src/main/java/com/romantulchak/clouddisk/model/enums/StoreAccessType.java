package com.romantulchak.clouddisk.model.enums;

import com.romantulchak.clouddisk.dto.StoreAccessDTO;

import java.util.Arrays;
import java.util.EnumSet;
import java.util.List;
import java.util.stream.Collectors;

public enum StoreAccessType {
    READER("Reader"), COMMENTATOR("Commentator"), EDITOR("Editor");

    private String value;

    StoreAccessType(String value){
        this.value = value;
    }

    public static boolean findAccessType(String type) {
        return Arrays.stream(StoreAccessType.values())
                .anyMatch(storeAccessType -> storeAccessType.name().equals(type));
    }

    public static List<StoreAccessDTO> getAccessTypes(){
        return EnumSet.allOf(StoreAccessType.class).stream()
                .map(storeAccessType -> storeAccessType.convertToDTO(storeAccessType))
                .collect(Collectors.toList());
    }

    private StoreAccessDTO convertToDTO(StoreAccessType storeAccessType){
        return new StoreAccessDTO(storeAccessType.name(), storeAccessType.getValue());
    }

    public String getValue() {
        return value;
    }
}
