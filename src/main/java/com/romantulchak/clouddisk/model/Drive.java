package com.romantulchak.clouddisk.model;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.List;

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
}
