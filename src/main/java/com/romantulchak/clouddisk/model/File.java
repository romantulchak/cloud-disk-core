package com.romantulchak.clouddisk.model;

import com.romantulchak.clouddisk.model.enums.RemoveType;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@DiscriminatorValue(value = "File")
public class File extends StoreAbstract {

    private long size;

    @ManyToOne(fetch = FetchType.LAZY)
    private Folder folder;

    public long getSize() {
        return size;
    }

    public File setSize(long size) {
        this.size = size;
        return this;
    }

    public Folder getFolder() {
        return folder;
    }

    public File setFolder(Folder folder) {
        this.folder = folder;
        return this;
    }

    @Override
    public File setId(long id) {
        super.setId(id);
        return this;
    }

    @Override
    public File setName(String name) {
        super.setName(name);
        return this;
    }

    @Override
    public File setCreateAt(LocalDateTime createAt) {
        super.setCreateAt(createAt);
        return this;

    }

    @Override
    public File setUploadAt(LocalDateTime uploadAt) {
        super.setUploadAt(uploadAt);
        return this;
    }

    @Override
    public File setTrash(Trash trash) {
        super.setTrash(trash);
        return this;
    }

    @Override
    public File setPath(LocalPath path) {
        super.setPath(path);
        return this;
    }

    @Override
    public File setRemoveType(RemoveType removeType) {
        super.setRemoveType(removeType);
        return this;
    }

    @Override
    public File setDrive(Drive drive) {
        super.setDrive(drive);
        return this;
    }

    @Override
    public File setLink(UUID link) {
        super.setLink(link);
        return this;
    }

    @Override
    public File setHasLinkAccess(boolean hasLinkAccess) {
        super.setHasLinkAccess(hasLinkAccess);
        return this;
    }

    @Override
    public File setOwner(User owner) {
        super.setOwner(owner);
        return this;
    }

    @Override
    public File setRemove(PreRemove remove) {
        super.setRemove(remove);
        return this;
    }

    @Override
    public File setRootFolder(UUID rootFolder) {
        super.setRootFolder(rootFolder);
        return this;
    }
}

