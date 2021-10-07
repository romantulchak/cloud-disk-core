package com.romantulchak.clouddisk.service;

import com.romantulchak.clouddisk.model.Drive;
import com.romantulchak.clouddisk.model.Store;
import com.romantulchak.clouddisk.model.Trash;

import java.util.List;

public interface TrashService {

    Trash create(Drive drive);

    Trash getTrashByDriveName(String driveName);

    List<Store> getRemovedElements(String driveName);
}
