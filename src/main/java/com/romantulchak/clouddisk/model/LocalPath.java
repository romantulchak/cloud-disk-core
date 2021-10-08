package com.romantulchak.clouddisk.model;

import javax.persistence.Embeddable;

@Embeddable
public class LocalPath {

    private String fullPath;
    private String shortPath;
    private String oldPath;

    public LocalPath(String fullPath, String shortPath) {
        this.fullPath = fullPath;
        this.shortPath = shortPath;
    }



    public LocalPath() {

    }

    public String getFullPath() {
        return fullPath;
    }

    public LocalPath setFullPath(String fullPath) {
        this.fullPath = fullPath;
        return this;
    }

    public String getShortPath() {
        return shortPath;
    }

    public LocalPath setShortPath(String shortPath) {
        this.shortPath = shortPath;
        return this;
    }

    public String getOldPath() {
        return oldPath;
    }

    public LocalPath setOldPath(String oldPath) {
        this.oldPath = oldPath;
        return this;
    }
}
