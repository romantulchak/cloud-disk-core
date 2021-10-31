package com.romantulchak.clouddisk.service;

import com.romantulchak.clouddisk.model.Store;

import java.util.List;
import java.util.UUID;

public interface ElementService {

    void restoreElement(UUID elementLink);

    void preRemoveElement(UUID elementLink, String driveName);

    void removeElement(UUID link);

    List<Store> findRemovedElements(String driveName);

}
