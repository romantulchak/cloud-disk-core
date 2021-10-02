package com.romantulchak.clouddisk.dto;

import com.fasterxml.jackson.annotation.JsonView;
import com.mapperDTO.annotation.DTO;
import com.mapperDTO.annotation.MapToDTO;
import com.romantulchak.clouddisk.model.Store;
import com.romantulchak.clouddisk.model.View;
import com.romantulchak.clouddisk.model.enums.ContextType;

import java.time.LocalDateTime;
import java.util.UUID;

@DTO
public class FileDTO implements Store {

    @JsonView(View.FolderFileView.class)
    @MapToDTO(mapClass = {View.FolderFileView.class})
    private long id;

    @JsonView(View.FolderFileView.class)
    @MapToDTO(mapClass = {View.FolderFileView.class})
    private String name;

    @JsonView(View.FolderFileView.class)
    @MapToDTO(mapClass = {View.FolderFileView.class})
    private LocalDateTime createAt;

    private LocalDateTime uploadAt;

    @JsonView(View.FolderFileView.class)
    @MapToDTO(mapClass = {View.FolderFileView.class})
    private int size;

    private FolderDTO folder;

    private DriveDTO drive;

    @JsonView(View.FolderFileView.class)
    @MapToDTO(mapClass = {View.FolderFileView.class})
    private UUID link;

    private boolean hasLinkAccess;

    @JsonView(View.FolderFileView.class)
    @MapToDTO(mapClass = {View.FolderFileView.class})
    private UserDTO owner;

    private String extension;

    @JsonView(View.FolderFileView.class)
    @MapToDTO(mapClass = {View.FolderFileView.class})
    private ContextType context = ContextType.FILE;

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

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public FolderDTO getFolder() {
        return folder;
    }

    public void setFolder(FolderDTO folder) {
        this.folder = folder;
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

    public boolean isHasLinkAccess() {
        return hasLinkAccess;
    }

    public void setHasLinkAccess(boolean hasLinkAccess) {
        this.hasLinkAccess = hasLinkAccess;
    }

    public UserDTO getOwner() {
        return owner;
    }

    public void setOwner(UserDTO owner) {
        this.owner = owner;
    }

    public String getExtension() {
        return extension;
    }

    public void setExtension(String extension) {
        this.extension = extension;
    }

    public ContextType getContext() {
        return context;
    }

    public void setContext(ContextType context) {
        this.context = context;
    }
}
