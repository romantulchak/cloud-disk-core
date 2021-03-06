package com.romantulchak.clouddisk.model;

import com.romantulchak.clouddisk.model.enums.FolderColorType;
import com.romantulchak.clouddisk.model.enums.RemoveType;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Entity
@DiscriminatorValue(value = "Folder")
public class Folder extends StoreAbstract {

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "folder", orphanRemoval = true)
    private List<File> files;

    private String color;

    @Transient
    private String mainFolderIdentifier;

    public Folder() {
        this.color = FolderColorType.MOUSE.getColor();
    }

    @Override
    public Folder setRootFolder(UUID rootFolder) {
        super.setRootFolder(rootFolder);
        return this;
    }

    public List<File> getFiles() {
        return files;
    }

    public Folder setFiles(List<File> files) {
        this.files = files;
        return this;
    }

    @Override
    public Folder setId(long id) {
        super.setId(id);
        return this;
    }

    @Override
    public Folder setName(String name) {
        super.setName(name);
        return this;
    }

    @Override
    public Folder setCreateAt(LocalDateTime createAt) {
        super.setCreateAt(createAt);
        return this;

    }

    @Override
    public Folder setUploadAt(LocalDateTime uploadAt) {
        super.setUploadAt(uploadAt);
        return this;
    }

    @Override
    public Folder setTrash(Trash trash) {
        super.setTrash(trash);
        return this;
    }

    @Override
    public Folder setPath(LocalPath path) {
        super.setPath(path);
        return this;
    }

    @Override
    public Folder setRemoveType(RemoveType removeType) {
        super.setRemoveType(removeType);
        return this;
    }

    @Override
    public Folder setDrive(Drive drive) {
        super.setDrive(drive);
        return this;
    }

    @Override
    public Folder setLink(UUID link) {
        super.setLink(link);
        return this;
    }

    @Override
    public Folder setHasLinkAccess(boolean hasLinkAccess) {
        super.setHasLinkAccess(hasLinkAccess);
        return this;
    }

    @Override
    public Folder setOwner(User owner) {
        super.setOwner(owner);
        return this;
    }

    @Override
    public Folder setRemove(PreRemove remove) {
        super.setRemove(remove);
        return this;
    }

    public String getColor() {
        return color;
    }

    public Folder setColor(String color) {
        this.color = color;
        return this;
    }

    public String getMainFolderIdentifier() {
        return mainFolderIdentifier;
    }

    public Folder setMainFolderIdentifier(String mainFolderIdentifier) {
        this.mainFolderIdentifier = mainFolderIdentifier;
        return this;
    }
}
