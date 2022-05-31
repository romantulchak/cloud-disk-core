package com.romantulchak.clouddisk.service;

import com.romantulchak.clouddisk.model.Store;
import com.romantulchak.clouddisk.model.enums.RemoveType;

import java.util.List;
import java.util.UUID;

public interface ElementService {

    /**
     * Finds element by its link if not found throw {@link com.romantulchak.clouddisk.exception.ElementNotFoundException}
     * after that moves file from trash to previous directory
     * if element is of {@link com.romantulchak.clouddisk.model.File} that also move preview image
     * to previous directory if all files successfully moved change element status and persist to DB
     *
     * @param elementLink to get element which will be restored
     */
    void restoreElement(UUID elementLink);

    /**
     * Finds element by link and move files to trash
     * if files successfully moved to trash that change status
     * of element to {@link RemoveType.PRE_REMOVED}
     *
     * @param elementLink to get element which will be moved to trash
     * @param driveName to get trash folder by drive name
     */
    void preRemoveElement(UUID elementLink, String driveName);

    void removeElement(UUID link);

    List<Store> findRemovedElements(String driveName);

    List<Store> findElementsForDrive(String driveName);

    void renameElement(String name, UUID link);
}
