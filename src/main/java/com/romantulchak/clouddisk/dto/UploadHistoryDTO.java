package com.romantulchak.clouddisk.dto;

import com.fasterxml.jackson.annotation.JsonView;
import com.mapperDTO.annotation.DTO;
import com.mapperDTO.annotation.MapToDTO;
import com.romantulchak.clouddisk.model.View;
import com.romantulchak.clouddisk.model.enums.ContextType;

import java.util.UUID;

@DTO
public class UploadHistoryDTO extends HistoryDTO {

    @MapToDTO(mapClass = {View.HistoryView.class})
    @JsonView(View.HistoryView.class)
    private String uploadedElementName;

    @MapToDTO(mapClass = {View.HistoryView.class})
    @JsonView(View.HistoryView.class)
    private UUID uploadedElementLink;

    @MapToDTO(mapClass = {View.HistoryView.class})
    @JsonView(View.HistoryView.class)
    private String uploadedElementFullPath;

    @MapToDTO(mapClass = {View.HistoryView.class})
    @JsonView(View.HistoryView.class)
    private ContextType context;

    public UploadHistoryDTO(){

    }

    public UploadHistoryDTO(String uploadedElementName, UUID uploadedElementLink, String uploadedElementFullPath, ContextType context) {
        this.uploadedElementName = uploadedElementName;
        this.uploadedElementLink = uploadedElementLink;
        this.uploadedElementFullPath = uploadedElementFullPath;
        this.context = context;
    }

    public String getUploadedElementName() {
        return uploadedElementName;
    }

    public void setUploadedElementName(String uploadedElementName) {
        this.uploadedElementName = uploadedElementName;
    }

    public UUID getUploadedElementLink() {
        return uploadedElementLink;
    }

    public void setUploadedElementLink(UUID uploadedElementLink) {
        this.uploadedElementLink = uploadedElementLink;
    }

    public String getUploadedElementFullPath() {
        return uploadedElementFullPath;
    }

    public void setUploadedElementFullPath(String uploadedElementFullPath) {
        this.uploadedElementFullPath = uploadedElementFullPath;
    }

    public ContextType getContext() {
        return context;
    }

    public void setContext(ContextType context) {
        this.context = context;
    }
}
