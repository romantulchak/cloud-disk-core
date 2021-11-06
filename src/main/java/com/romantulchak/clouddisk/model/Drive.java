package com.romantulchak.clouddisk.model;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Entity
public class Drive {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(unique = true)
    @Size(max = 100)
    private String name;

    @OneToMany(mappedBy = "drive", fetch = FetchType.LAZY, orphanRemoval = true)
    private List<Folder> folders;

    private LocalDateTime createAt;

    @OneToOne
    private User owner;

    @ManyToOne
    private Plan plan;

    @OneToMany(mappedBy = "drive", fetch = FetchType.LAZY, orphanRemoval = true)
    private List<File> files;

    private String fullPath;

    private String shortPath;

    @OneToOne(mappedBy = "drive")
    private Trash trash;

    public long getId() {
        return id;
    }

    public Drive setId(long id) {
        this.id = id;
        return this;
    }

    public List<Folder> getFolders() {
        return folders;
    }

    public Drive setFolders(List<Folder> folders) {
        this.folders = folders;
        return this;
    }

    public User getOwner() {
        return owner;
    }

    public Drive setOwner(User owner) {
        this.owner = owner;
        return this;
    }

    public String getName() {
        return name;
    }

    public Drive setName(String name) {
        this.name = name;
        return this;
    }

    public LocalDateTime getCreateAt() {
        return createAt;
    }

    public Drive setCreateAt(LocalDateTime createAt) {
        this.createAt = createAt;
        return this;
    }

    public Plan getPlan() {
        return plan;
    }

    public Drive setPlan(Plan plan) {
        this.plan = plan;
        return this;
    }

    public List<File> getFiles() {
        return files;
    }

    public Drive setFiles(List<File> files) {
        this.files = files;
        return this;
    }

    public String getFullPath() {
        return fullPath;
    }

    public Drive setFullPath(String fullPath) {
        this.fullPath = fullPath;
        return this;
    }

    public String getShortPath() {
        return shortPath;
    }

    public Drive setShortPath(String shortPath) {
        this.shortPath = shortPath;
        return this;
    }

    public Trash getTrash() {
        return trash;
    }

    public Drive setTrash(Trash trash) {
        this.trash = trash;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Drive drive = (Drive) o;
        return id == drive.id && Objects.equals(name, drive.name) && Objects.equals(owner, drive.owner) && Objects.equals(fullPath, drive.fullPath) && Objects.equals(shortPath, drive.shortPath);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, owner, fullPath, shortPath);
    }
}
