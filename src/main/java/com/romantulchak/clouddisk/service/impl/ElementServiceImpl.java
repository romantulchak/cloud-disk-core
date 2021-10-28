package com.romantulchak.clouddisk.service.impl;

import com.romantulchak.clouddisk.exception.FileNotFoundException;
import com.romantulchak.clouddisk.model.LocalPath;
import com.romantulchak.clouddisk.model.StoreAbstract;
import com.romantulchak.clouddisk.model.enums.RemoveType;
import com.romantulchak.clouddisk.repository.PreRemoveRepository;
import com.romantulchak.clouddisk.repository.StoreRepository;
import com.romantulchak.clouddisk.service.ElementService;
import com.romantulchak.clouddisk.utils.FolderUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class ElementServiceImpl implements ElementService {

    private final StoreRepository storeRepository;
    private final FolderUtils folderUtils;
    private final PreRemoveRepository removeRepository;

    @Autowired
    public ElementServiceImpl(StoreRepository storeRepository,
                              FolderUtils folderUtils,
                              PreRemoveRepository removeRepository) {
        this.storeRepository = storeRepository;
        this.folderUtils = folderUtils;
        this.removeRepository = removeRepository;
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
}
