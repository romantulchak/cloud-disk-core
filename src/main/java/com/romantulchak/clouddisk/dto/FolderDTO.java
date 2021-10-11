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
public class FolderDTO implements Store {

    @JsonView({View.FolderFileView.class,View.FolderView.class})
    @MapToDTO(mapClass = {View.FolderFileView.class,View.FolderView.class})
    private long id;

    @JsonView({View.FolderFileView.class,View.FolderView.class})
    @MapToDTO(mapClass = {View.FolderFileView.class,View.FolderView.class})
    private String name;

    @JsonView({View.FolderFileView.class,View.FolderView.class})
    @MapToDTO(mapClass = {View.FolderFileView.class,View.FolderView.class})
    private String color;

    private List<FolderDTO> subFolders;

    private DriveDTO drive;

    @JsonView({View.FolderFileView.class,View.FolderView.class})
    @MapToDTO(mapClass = {View.FolderFileView.class,View.FolderView.class})
    private UUID link;

    @JsonView({View.FolderFileView.class,View.FolderView.class})
    @MapToDTO(mapClass = {View.FolderFileView.class,View.FolderView.class})
    private LocalDateTime createAt;

    private LocalDateTime uploadAt;

    @JsonView({View.FolderFileView.class,View.FolderView.class})
    @MapToDTO(mapClass = {View.FolderFileView.class,View.FolderView.class})
    private UserDTO owner;

    private boolean hasLinkAccess;

    private List<FileDTO> files;

    @JsonView({View.FolderFileView.class,View.FolderView.class})
    @MapToDTO(mapClass = {View.FolderFileView.class,View.FolderView.class})
    private ContextType context = ContextType.FOLDER;

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

    public List<FolderDTO> getSubFolders() {
        return subFolders;
    }

    public void setSubFolders(List<FolderDTO> subFolders) {
        this.subFolders = subFolders;
    }

    public DriveDTO getDrive() {
        return drive;
    }

    public void setDrive(DriveDTO drive) {
        this.drive = drive;
    }

    public UUID getLink() {
        return link;
    }

    public void setLink(UUID link) {
        this.link = link;
    }

    public LocalDateTime getCreateAt() {
        return createAt;
    }

    public void setCreateAt(LocalDateTime createAt) {
        this.createAt = createAt;
    }

    public LocalDateTime getUploadAt() {
        return uploadAt;
    }

    public void setUploadAt(LocalDateTime uploadAt) {
        this.uploadAt = uploadAt;
    }

    public UserDTO getOwner() {
        return owner;
    }

    public void setOwner(UserDTO owner) {
        this.owner = owner;
    }

    public boolean isHasLinkAccess() {
        return hasLinkAccess;
    }

    public void setHasLinkAccess(boolean hasLinkAccess) {
        this.hasLinkAccess = hasLinkAccess;
    }

    public List<FileDTO> getFiles() {
        return files;
    }

    public void setFiles(List<FileDTO> files) {
        this.files = files;
    }

    public ContextType getContext() {
        return context;
    }

    public void setContext(ContextType context) {
        this.context = context;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }
}
