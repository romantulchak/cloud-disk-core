package com.romantulchak.clouddisk.projection;

import com.romantulchak.clouddisk.model.User;

public interface FolderOwnerProjection {

    User getOwner();

    boolean isHasLinkAccess();
}
