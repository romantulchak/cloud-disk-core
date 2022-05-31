package com.romantulchak.clouddisk.utils;

import com.romantulchak.clouddisk.constant.ApplicationConstant;
import com.romantulchak.clouddisk.dto.StoreAbstractDTO;
import com.romantulchak.clouddisk.model.File;
import com.romantulchak.clouddisk.model.LocalPath;
import com.romantulchak.clouddisk.model.StoreAbstract;
import com.romantulchak.clouddisk.model.Trash;
import com.romantulchak.clouddisk.model.enums.ContextType;
import com.romantulchak.clouddisk.service.impl.UserDetailsImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class StoreUtils {

    private static final Logger LOGGER = LoggerFactory.getLogger(StoreUtils.class);

    private StoreUtils() {

    }

    public static int parseIf(String pageAsString) {
        if (pageAsString == null || pageAsString.isEmpty()) {
            return 0;
        }
        int page;
        try {
            page = Integer.parseInt(pageAsString);
        } catch (NumberFormatException e) {
            LOGGER.error("String {} has invalid format", pageAsString);
            page = 0;
        }
        return page;
    }

    public static Map<String, LocalPath> preRemoveElement(LocalPath localPath, FolderUtils folderUtils, Trash trash) {
        LocalPath path = folderUtils.moveFileToTrash(localPath.getShortPath(), trash.getPath(), FileUtils.getName(localPath.getShortPath()));
        if (path != null) {
            LocalPath newPath = new LocalPath()
                    .setOldFullPath(localPath.getFullPath())
                    .setOldShortPath(localPath.getShortPath())
                    .setShortPath(path.getShortPath())
                    .setFullPath(path.getFullPath());
            return Map.of(ApplicationConstant.OLD_PATH, path, ApplicationConstant.NEW_PATH, newPath);
        }
        return Map.of();
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
