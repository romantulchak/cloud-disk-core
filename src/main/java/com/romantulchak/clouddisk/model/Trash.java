package com.romantulchak.clouddisk.model;

import javax.persistence.*;
import java.util.List;

@Entity
public class Trash {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(unique = true)
    private String name;

    @OneToOne
    private Drive drive;

    @OneToMany(mappedBy = "trash")
    private List<File> files;

    @OneToMany(mappedBy = "trash")
    private List<Folder> folders;

    @Embedded
    private LocalPath path;

    public long getId() {
        return id;
    }

    public Trash setId(long id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public Trash setName(String name) {
        this.name = name;
        return this;
    }

    public Drive getDrive() {
        return drive;
    }

    public Trash setDrive(Drive drive) {
        this.drive = drive;
        return this;
    }

    public List<File> getFiles() {
        return files;
    }

    public Trash setFiles(List<File> files) {
        this.files = files;
        return this;
    }

    public List<Folder> getFolders() {
        return folders;
    }

    public Trash setFolders(List<Folder> folders) {
        this.folders = folders;
        return this;
    }

    public LocalPath getPath() {
        return path;
    }

    public Trash setPath(LocalPath path) {
        this.path = path;
        return this;
    }
}
