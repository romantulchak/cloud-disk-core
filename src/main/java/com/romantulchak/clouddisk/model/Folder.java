package com.romantulchak.clouddisk.model;

import com.romantulchak.clouddisk.model.enums.RemoveType;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@NamedEntityGraph(
        name = "Folder.subFolders",
        attributeNodes = @NamedAttributeNode("subFolders")
)
@Entity
public class Folder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(unique = true)
    @Size(max = 90)
    private String name;

    @OneToMany(orphanRemoval = true)
    @JoinTable(name = "folder_subFolders")
    private List<Folder> subFolders;

    @ManyToOne
    private Drive drive;

    private UUID link;

    private LocalDateTime createAt;

    private LocalDateTime uploadAt;

    @ManyToOne
    private User owner;

    private boolean hasLinkAccess;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "folder", orphanRemoval = true)
    private List<File> files;

    @ManyToOne
    private Trash trash;

    @Embedded
    private LocalPath path;

    @Enumerated(EnumType.STRING)
    private RemoveType removeType = RemoveType.SAVED;

    public long getId() {
        return id;
    }

    public Folder setId(long id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public Folder setName(String name) {
        this.name = name;
        return this;
    }

    public List<Folder> getSubFolders() {
        return subFolders;
    }

    public Folder setSubFolders(List<Folder> subFolders) {
        this.subFolders = subFolders;
        return this;
    }

    public Drive getDrive() {
        return drive;
    }

    public Folder setDrive(Drive drive) {
        this.drive = drive;
        return this;
    }

    public boolean isHasLinkAccess() {
        return hasLinkAccess;
    }

    public Folder setHasLinkAccess(boolean hasLinkAccess) {
        this.hasLinkAccess = hasLinkAccess;
        return this;
    }

    public UUID getLink() {
        return link;
    }

    public Folder setLink(UUID link) {
        this.link = link;
        return this;
    }

    public LocalDateTime getCreateAt() {
        return createAt;
    }

    public Folder setCreateAt(LocalDateTime createAt) {
        this.createAt = createAt;
        return this;
    }

    public User getOwner() {
        return owner;
    }

    public Folder setOwner(User owner) {
        this.owner = owner;
        return this;
    }

    public List<File> getFiles() {
        return files;
    }

    public Folder setFiles(List<File> files) {
        this.files = files;
        return this;
    }

    public LocalDateTime getUploadAt() {
        return uploadAt;
    }

    public Folder setUploadAt(LocalDateTime uploadAt) {
        this.uploadAt = uploadAt;
        return this;
    }

    public Trash getTrash() {
        return trash;
    }

    public Folder setTrash(Trash trash) {
        this.trash = trash;
        return this;
    }

    public LocalPath getPath() {
        return path;
    }

    public Folder setPath(LocalPath path) {
        this.path = path;
        return this;
    }

    public RemoveType getRemoveType() {
        return removeType;
    }

    public Folder setRemoveType(RemoveType removeType) {
        this.removeType = removeType;
        return this;
    }
}
