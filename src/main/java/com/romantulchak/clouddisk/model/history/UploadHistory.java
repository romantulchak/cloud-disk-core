package com.romantulchak.clouddisk.model.history;

import com.romantulchak.clouddisk.model.StoreAbstract;
import com.romantulchak.clouddisk.model.User;
import com.romantulchak.clouddisk.model.enums.ContextType;
import com.romantulchak.clouddisk.model.enums.HistoryType;

import javax.persistence.*;
import java.util.UUID;

@Entity
@DiscriminatorValue(value = "Upload")
public class UploadHistory extends History {

    private String uploadedElementName;

    private UUID uploadedElementLink;

    private String uploadedElementFullPath;

    @Enumerated(EnumType.STRING)
    private ContextType context;

    public UploadHistory() {
    }

    public UploadHistory(StoreAbstract element, HistoryType type, User user, String uploadElementName, UUID uploadedElementLink, String uploadElementFullPath, ContextType context) {
        super(element, type, user);
        this.uploadedElementName = uploadElementName;
        this.uploadedElementLink = uploadedElementLink;
        this.uploadedElementFullPath = uploadElementFullPath;
        this.context = context;
    }

    public ContextType getContext() {
        return context;
    }

    public void setContext(ContextType context) {
        this.context = context;
    }

    public String getUploadedElementName() {
        return uploadedElementName;
    }

    public void setUploadedElementName(String name) {
        this.uploadedElementName = name;
    }

    public UUID getUploadedElementLink() {
        return uploadedElementLink;
    }

    public void setUploadedElementLink(UUID link) {
        this.uploadedElementLink = link;
    }

    public String getUploadedElementFullPath() {
        return uploadedElementFullPath;
    }

    public void setUploadedElementFullPath(String fullPath) {
        this.uploadedElementFullPath = fullPath;
    }
}
