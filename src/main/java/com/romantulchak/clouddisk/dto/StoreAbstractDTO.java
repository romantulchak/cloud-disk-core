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
public class StoreAbstractDTO implements Store {

    @MapToDTO(mapClass = {View.FolderFileView.class, View.FolderView.class})
    @JsonView({View.FolderFileView.class, View.FolderView.class})
    private long id;

    @MapToDTO(mapClass = {View.FolderFileView.class, View.FolderView.class})
    @JsonView({View.FolderFileView.class, View.FolderView.class})
    private String name;

    @MapToDTO(mapClass = {View.FolderFileView.class, View.FolderView.class})
    @JsonView({View.FolderFileView.class, View.FolderView.class})
    private LocalDateTime createAt;

    @MapToDTO(mapClass = {View.FolderFileView.class, View.FolderView.class})
    @JsonView({View.FolderFileView.class, View.FolderView.class})
    private UUID link;

    @MapToDTO(mapClass = {View.FolderFileView.class, View.FolderView.class})
    @JsonView({View.FolderFileView.class, View.FolderView.class})
    private UserDTO owner;

    @JsonView({View.FolderFileView.class,View.FolderView.class, View.FolderView.class})
    @MapToDTO(mapClass = {View.FolderFileView.class,View.FolderView.class})
    private ContextType context;

    @MapToDTO(mapClass = {View.FolderFileView.class, View.FolderView.class})
    @JsonView({View.FolderFileView.class, View.FolderView.class})
    private LocalPathDTO path;

    @MapToDTO(mapClass = {View.FolderFileView.class, View.FolderView.class})
    @JsonView({View.FolderFileView.class, View.FolderView.class})
    private boolean noticed;

    @MapToDTO(mapClass = {View.FolderFileView.class, View.FolderView.class})
    @JsonView({View.FolderFileView.class, View.FolderView.class})
    private ElementAccessDTO access;

    @JsonView({View.FolderFileView.class, View.FolderView.class})
    private boolean isOwner;

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

    public UUID getLink() {
        return link;
    }

    public void setLink(UUID link) {
        this.link = link;
    }

    public UserDTO getOwner() {
        return owner;
    }

    public void setOwner(UserDTO owner) {
        this.owner = owner;
    }

    public ContextType getContext() {
        return context;
    }

    public void setContext(ContextType context) {
        this.context = context;
    }

    public LocalPathDTO getPath() {
        return path;
    }

    public void setPath(LocalPathDTO path) {
        this.path = path;
    }

    public boolean isNoticed() {
        return noticed;
    }

    public StoreAbstractDTO setNoticed(boolean noticed) {
        this.noticed = noticed;
        return this;
    }

    public ElementAccessDTO getAccess() {
        return access;
    }

    public void setAccess(ElementAccessDTO access) {
        this.access = access;
    }

    public boolean isOwner() {
        return isOwner;
    }

    public StoreAbstractDTO setOwner(boolean owner) {
        isOwner = owner;
        return this;
    }
}
