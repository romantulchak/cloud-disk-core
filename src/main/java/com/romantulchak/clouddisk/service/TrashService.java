package com.romantulchak.clouddisk.service;

import com.romantulchak.clouddisk.model.Drive;
import com.romantulchak.clouddisk.model.Trash;

public interface TrashService {

    Trash create(Drive drive);

    Trash getTrashByDriveName(String driveName);

}
