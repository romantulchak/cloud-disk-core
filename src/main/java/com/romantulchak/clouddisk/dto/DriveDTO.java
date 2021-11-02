package com.romantulchak.clouddisk.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonView;
import com.mapperDTO.annotation.DTO;
import com.mapperDTO.annotation.MapToDTO;
import com.romantulchak.clouddisk.model.View;

import java.time.LocalDateTime;
import java.util.List;

@DTO
public class DriveDTO {

    @JsonView(View.DriveView.class)
    @MapToDTO(mapClass = {View.DriveView.class})
    private long id;

    @JsonView(View.DriveView.class)
    @MapToDTO(mapClass = {View.DriveView.class})
    private String name;

    private List<FolderDTO> folders;

    private LocalDateTime createAt;

    private UserDTO owner;

    private PlanDTO plan;

    private List<FileDTO> files;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<FolderDTO> getFolders() {
        return folders;
    }

    public void setFolders(List<FolderDTO> folders) {
        this.folders = folders;
    }

    public LocalDateTime getCreateAt() {
        return createAt;
    }

    public void setCreateAt(LocalDateTime createAt) {
        this.createAt = createAt;
    }

    public UserDTO getOwner() {
        return owner;
    }

    public void setOwner(UserDTO owner) {
        this.owner = owner;
    }

    public PlanDTO getPlan() {
        return plan;
    }

    public void setPlan(PlanDTO plan) {
        this.plan = plan;
    }

    public List<FileDTO> getFiles() {
        return files;
    }

    public void setFiles(List<FileDTO> files) {
        this.files = files;
    }
}
