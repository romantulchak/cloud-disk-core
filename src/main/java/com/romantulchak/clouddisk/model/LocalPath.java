package com.romantulchak.clouddisk.model;

public class LocalPath {

    private String fullPath;
    private String shortPath;

    public LocalPath(String fullPath, String shortPath) {
        this.fullPath = fullPath;
        this.shortPath = shortPath;
    }

    public String getFullPath() {
        return fullPath;
    }

    public void setFullPath(String fullPath) {
        this.fullPath = fullPath;
    }

    public String getShortPath() {
        return shortPath;
    }

    public void setShortPath(String shortPath) {
        this.shortPath = shortPath;
    }
}
