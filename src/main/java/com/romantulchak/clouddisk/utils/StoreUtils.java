package com.romantulchak.clouddisk.utils;

import com.romantulchak.clouddisk.dto.StoreAbstractDTO;
import com.romantulchak.clouddisk.model.File;
import com.romantulchak.clouddisk.model.LocalPath;
import com.romantulchak.clouddisk.model.StoreAbstract;
import com.romantulchak.clouddisk.model.Trash;
import com.romantulchak.clouddisk.model.enums.ContextType;
import com.romantulchak.clouddisk.model.enums.RemoveType;
import com.romantulchak.clouddisk.service.impl.UserDetailsImpl;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class StoreUtils {

    private StoreUtils() {

    }

    public static LocalPath preRemoveElement(StoreAbstract element, FolderUtils folderUtils, Trash trash) {
        LocalPath path = folderUtils.moveFileToTrash(element.getPath().getShortPath(), trash.getPath(), FileUtils.getName(element.getPath().getShortPath()));
        if (path != null) {
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
        return null;
    }

    public static void setContext(StoreAbstract store, StoreAbstractDTO storeDTO) {
        ContextType context;
        if (store.getClass().isAssignableFrom(File.class)) {
            context = ContextType.FILE;
        } else {
            context = ContextType.FOLDER;
        }
        storeDTO.setContext(context);
    }

    public static ContextType getContext(StoreAbstract element) {
        if (element.getClass().isAssignableFrom(File.class)) {
            return ContextType.FILE;
        } else {
            return ContextType.FOLDER;
        }
    }

    public static boolean isStarred(StoreAbstract element) {
        if (element.getStarreds() != null && !element.getStarreds().isEmpty()) {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
            return element.getStarreds().stream()
                    .anyMatch(starred -> starred.getElement().getId() == element.getId()
                            && starred.getUser().getId() == userDetails.getId());

        }
        return false;
    }

    public static boolean isOwner(StoreAbstract element) {
        Authentication authentication = SecurityContextHolder
                .getContext()
                .getAuthentication();
        if (authentication == null) {
            return false;
        }
        UserDetailsImpl principal = (UserDetailsImpl) authentication.getPrincipal();
        return element.getOwner().getId() == principal.getId();
    }
}
