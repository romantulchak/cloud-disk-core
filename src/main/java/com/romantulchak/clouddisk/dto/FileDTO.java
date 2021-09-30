package com.romantulchak.clouddisk.dto;

import com.mapperDTO.annotation.DTO;

import java.time.LocalDateTime;
import java.util.UUID;

@DTO
public class FileDTO {

    private long id;

    private String name;

    private LocalDateTime createAt;

    private LocalDateTime uploadAt;

    private int size;

    private FolderDTO folder;

    private DriveDTO drive;

    private UUID link;

    private boolean hasLinkAccess;

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
}
