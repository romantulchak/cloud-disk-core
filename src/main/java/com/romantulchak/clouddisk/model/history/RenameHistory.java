package com.romantulchak.clouddisk.model.history;

import com.romantulchak.clouddisk.model.StoreAbstract;
import com.romantulchak.clouddisk.model.User;
import com.romantulchak.clouddisk.model.enums.HistoryType;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.validation.constraints.Size;

@Entity
@DiscriminatorValue(value = "Rename")
public class RenameHistory extends History {

    @Size(max = 195)
    private String name;

    @Size(max = 195)
    private String oldName;

    public RenameHistory() {
    }

    public RenameHistory(StoreAbstract element, User user, String name) {
        super(element, HistoryType.RENAME, user);
        this.name = name;
        this.oldName = element.getOldName();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOldName() {
        return oldName;
    }

    public void setOldName(String oldName) {
        this.oldName = oldName;
    }
}
