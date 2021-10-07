package com.romantulchak.clouddisk.model;

import com.romantulchak.clouddisk.model.enums.RemoveType;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
public class File {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Size(max = 195)
    @Column(unique = true)
    private String name;

    private LocalDateTime createAt;

    private LocalDateTime uploadAt;

    private long size;

    @ManyToOne
    private Folder folder;

    @ManyToOne
    private Drive drive;

    private UUID link;

    private boolean hasLinkAccess;

    @ManyToOne
    private User owner;

    private String extension;

    @ManyToOne
    private Trash trash;

    @Embedded
    private LocalPath path;

    @Enumerated(EnumType.STRING)
    private RemoveType removeType = RemoveType.SAVED;

    public long getId() {
        return id;
    }

    public File setId(long id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public File setName(String name) {
        this.name = name;
        return this;
    }

    public LocalDateTime getCreateAt() {
        return createAt;
    }

    public File setCreateAt(LocalDateTime createAt) {
        this.createAt = createAt;
        return this;
    }

    public LocalDateTime getUploadAt() {
        return uploadAt;
    }

    public File setUploadAt(LocalDateTime uploadAt) {
        this.uploadAt = uploadAt;
        return this;
    }

    public Folder getFolder() {
        return folder;
    }

    public File setFolder(Folder folder) {
        this.folder = folder;
        return this;
    }

    public Drive getDrive() {
        return drive;
    }

    public File setDrive(Drive drive) {
        this.drive = drive;
        return this;
    }

    public long getSize() {
        return size;
    }

    public File setSize(long size) {
        this.size = size;
        return this;
    }

    public UUID getLink() {
        return link;
    }

    public File setLink(UUID link) {
        this.link = link;
        return this;
    }

    public boolean isHasLinkAccess() {
        return hasLinkAccess;
    }

    public File setHasLinkAccess(boolean hasLinkAccess) {
        this.hasLinkAccess = hasLinkAccess;
        return this;
    }

    public User getOwner() {
        return owner;
    }

    public File setOwner(User owner) {
        this.owner = owner;
        return this;
    }

    public String getExtension() {
        return extension;
    }

    public File setExtension(String extension) {
        this.extension = extension;
        return this;
    }

    public Trash getTrash() {
        return trash;
    }

    public File setTrash(Trash trash) {
        this.trash = trash;
        return this;
    }

    public LocalPath getPath() {
        return path;
    }

    public File setPath(LocalPath path) {
        this.path = path;
        return this;
    }

    public RemoveType getRemoveType() {
        return removeType;
    }

    public File setRemoveType(RemoveType removeType) {
        this.removeType = removeType;
        return this;
    }
}

