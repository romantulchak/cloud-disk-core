package com.romantulchak.clouddisk.model.enums;

import java.util.Arrays;
import java.util.List;

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

    public String getValue() {
        return value;
    }
}
