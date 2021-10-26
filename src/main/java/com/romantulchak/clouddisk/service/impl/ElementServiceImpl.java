package com.romantulchak.clouddisk.service.impl;

import com.romantulchak.clouddisk.exception.FileNotFoundException;
import com.romantulchak.clouddisk.exception.ObjectAlreadyPreRemovedException;
import com.romantulchak.clouddisk.model.*;
import com.romantulchak.clouddisk.model.enums.RemoveType;
import com.romantulchak.clouddisk.repository.PreRemoveRepository;
import com.romantulchak.clouddisk.repository.StoreRepository;
import com.romantulchak.clouddisk.service.ElementService;
import com.romantulchak.clouddisk.service.TrashService;
import com.romantulchak.clouddisk.utils.FolderUtils;
import com.romantulchak.clouddisk.utils.StoreUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class ElementServiceImpl implements ElementService {

    private final StoreRepository storeRepository;
    private final FolderUtils folderUtils;
    private final PreRemoveRepository removeRepository;
    private final TrashService trashService;

    @Autowired
    public ElementServiceImpl(StoreRepository storeRepository,
                              FolderUtils folderUtils,
                              PreRemoveRepository removeRepository,
                              TrashService trashService) {
        this.storeRepository = storeRepository;
        this.folderUtils = folderUtils;
        this.removeRepository = removeRepository;
        this.trashService = trashService;
    }

    @Transactional
    @Override
    public void restoreElement(UUID elementLink) {
        StoreAbstract element = storeRepository.findByLink(elementLink)
                .orElseThrow(() -> new FileNotFoundException(elementLink));
        if(element.getRemoveType() == RemoveType.PRE_REMOVED){
            LocalPath path = new LocalPath()
                    .setFullPath(element.getPath().getOldFullPath())
                    .setShortPath(element.getPath().getOldShortPath())
                    .setOldFullPath(element.getPath().getFullPath())
                    .setOldShortPath(element.getPath().getShortPath());
            element.setRemoveType(RemoveType.SAVED)
                    .setTrash(null)
                    .setPath(path);
            boolean isMoved = folderUtils.restoreElement(path.getOldShortPath(), path.getShortPath());
            removeRepository.deleteByElementId(element.getId());
            if(isMoved){
                storeRepository.save(element);
            }
        }
    }

    @Transactional
    @Override
    public void preRemoveElement(UUID elementLink, String driveName) {
        StoreAbstract element = storeRepository.findByLink(elementLink)
                .orElseThrow(() -> new FileNotFoundException(elementLink));
        if (element.getRemoveType() != RemoveType.PRE_REMOVED) {
            Trash trash = trashService.getTrashByDriveName(driveName);
            LocalPath path = StoreUtils.preRemoveElement(element, folderUtils, trash);
            storeRepository.save(element);
            createPreRemove(element, path.getShortPath());
        } else {
            throw new ObjectAlreadyPreRemovedException(element.getName());
        }
    }

    private void createPreRemove(StoreAbstract element, String pathInTrash) {
        PreRemove remove = new PreRemove(element, pathInTrash);
        removeRepository.save(remove);
    }
}
