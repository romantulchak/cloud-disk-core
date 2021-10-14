package com.romantulchak.clouddisk.utils;

import com.romantulchak.clouddisk.dto.FileDTO;
import com.romantulchak.clouddisk.dto.StoreAbstractDTO;
import com.romantulchak.clouddisk.model.File;
import com.romantulchak.clouddisk.model.LocalPath;
import com.romantulchak.clouddisk.model.StoreAbstract;
import com.romantulchak.clouddisk.model.Trash;
import com.romantulchak.clouddisk.model.enums.ContextType;
import com.romantulchak.clouddisk.model.enums.RemoveType;

public class StoreUtils {

    private StoreUtils(){}

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

    public static void setContext(StoreAbstract store, StoreAbstractDTO storeDTO){
        ContextType context;
        if(store.getClass().isAssignableFrom(File.class)){
            context = ContextType.FILE;
        }else{
            context = ContextType.FOLDER;
        }
        storeDTO.setContext(context);
    }
}
