package com.romantulchak.clouddisk.utils;

import com.romantulchak.clouddisk.model.LocalPath;
import com.romantulchak.clouddisk.model.StoreAbstract;
import com.romantulchak.clouddisk.model.Trash;
import com.romantulchak.clouddisk.model.enums.RemoveType;

public class StoreUtils {


    public static LocalPath preRemoveElement(StoreAbstract element, FolderUtils folderUtils, Trash trash){
        LocalPath path = folderUtils.moveFileToTrash(element.getPath().getShortPath(), trash.getPath(), element.getName());
        LocalPath newPath = new LocalPath()
                .setOldFullPath(element.getPath().getFullPath())
                .setOldShortPath(element.getPath().getShortPath())
                .setShortPath(path.getShortPath())
                .setFullPath(path.getFullPath());
        element.setRemoveType(RemoveType.PRE_REMOVED)
                .setTrash(trash)
                .setPath(newPath);
        return path;
    }
}
