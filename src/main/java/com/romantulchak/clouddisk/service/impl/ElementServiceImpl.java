package com.romantulchak.clouddisk.service.impl;

import com.mapperDTO.mapper.EntityMapperInvoker;
import com.romantulchak.clouddisk.constant.ApplicationConstant;
import com.romantulchak.clouddisk.dto.FileDTO;
import com.romantulchak.clouddisk.dto.FolderDTO;
import com.romantulchak.clouddisk.dto.StoreAbstractDTO;
import com.romantulchak.clouddisk.exception.ElementNameException;
import com.romantulchak.clouddisk.exception.ElementNotFoundException;
import com.romantulchak.clouddisk.exception.FileNotFoundException;
import com.romantulchak.clouddisk.exception.ObjectAlreadyPreRemovedException;
import com.romantulchak.clouddisk.model.*;
import com.romantulchak.clouddisk.model.enums.RemoveType;
import com.romantulchak.clouddisk.model.enums.HistoryType;
import com.romantulchak.clouddisk.repository.PreRemoveRepository;
import com.romantulchak.clouddisk.repository.StoreRepository;
import com.romantulchak.clouddisk.service.ElementService;
import com.romantulchak.clouddisk.service.HistoryService;
import com.romantulchak.clouddisk.service.TrashService;
import com.romantulchak.clouddisk.utils.FolderUtils;
import com.romantulchak.clouddisk.utils.StoreUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class ElementServiceImpl implements ElementService {

    private static final Logger LOGGER = LogManager.getLogger(ElementServiceImpl.class);
    private final StoreRepository storeRepository;
    private final FolderUtils folderUtils;
    private final PreRemoveRepository removeRepository;
    private final TrashService trashService;
    private final HistoryService historyService;
    private final EntityMapperInvoker<Folder, FolderDTO> folderMapper;
    private final EntityMapperInvoker<File, FileDTO> fileMapper;

    public ElementServiceImpl(StoreRepository storeRepository,
                              FolderUtils folderUtils,
                              PreRemoveRepository removeRepository,
                              TrashService trashService,
                              HistoryService historyService,
                              EntityMapperInvoker<Folder, FolderDTO> folderMapper,
                              EntityMapperInvoker<File, FileDTO> fileMapper) {
        this.storeRepository = storeRepository;
        this.folderUtils = folderUtils;
        this.removeRepository = removeRepository;
        this.trashService = trashService;
        this.folderMapper = folderMapper;
        this.fileMapper = fileMapper;
        this.historyService = historyService;
    }

    @Transactional
    @Override
    public void restoreElement(UUID elementLink) {
        StoreAbstract element = storeRepository.findByLink(elementLink)
                .orElseThrow(() -> new FileNotFoundException(elementLink));
        if (element.getRemoveType() == RemoveType.PRE_REMOVED) {
            LocalPath path = getRestoredPath(element.getPath());
            restorePreviewImage(element);
            element.setRemoveType(RemoveType.SAVED)
                    .setTrash(null)
                    .setPath(path);
            boolean isMoved = folderUtils.restoreElement(path.getOldShortPath(), path.getShortPath());
            removeRepository.deleteByElementId(element.getId());
            if (isMoved) {
                storeRepository.save(element);
                historyService.create(element, HistoryType.RESTORE);
            }
        }
    }

    private void restorePreviewImage(StoreAbstract element) {
        if (element instanceof File){
            File file = (File) element;
            LocalPath restoredPreviewPath = getRestoredPath(file.getPreviewPath());
            file.setPreviewPath(restoredPreviewPath);
            boolean isMoved = folderUtils.restoreElement(restoredPreviewPath.getOldShortPath(), restoredPreviewPath.getShortPath());
            if (!isMoved){
                LOGGER.error("Image preview for file {} could not moved", element.getName());
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
            Map<String, LocalPath> elementOldNewPathMap = StoreUtils.preRemoveElement(element.getPath(), folderUtils, trash);
            if (elementOldNewPathMap.isEmpty()){
                removeElement(elementLink);
            }else{
                preRemovePreviewImage(element, trash);
                LocalPath newPath = elementOldNewPathMap.get(ApplicationConstant.NEW_PATH);
                LocalPath oldPath = elementOldNewPathMap.get(ApplicationConstant.OLD_PATH);
                element.setRemoveType(RemoveType.PRE_REMOVED)
                        .setTrash(trash)
                        .setPath(newPath);
                storeRepository.save(element);
                historyService.create(element, HistoryType.PRE_REMOVE);
                createPreRemove(element, oldPath.getShortPath());
            }
        } else {
            throw new ObjectAlreadyPreRemovedException(element.getName());
        }
    }

    @Transactional
    @Override
    public void removeElement(UUID link) {
        StoreAbstract element = storeRepository.findByLink(link)
                .orElseThrow(() -> new FileNotFoundException(link));
        boolean isDeleted = folderUtils.removeElement(element.getPath().getShortPath());
        if (isDeleted) {
            removeRepository.deleteByElementId(element.getId());
            storeRepository.delete(element);
        }
    }

    @Override
    public List<Store> findRemovedElements(String driveName) {
        Trash trash = trashService.getTrashByDriveName(driveName);
        return storeRepository.findAllByTrashId(trash.getId())
                .stream()
                .sorted((Comparator.comparing(storeAbstract -> storeAbstract.getPreRemove().getAddedToTrash())))
                .map(element -> convertToDTO(element, View.FolderFileView.class))
                .collect(Collectors.toList());
    }

    @Override
    public List<Store> findElementsForDrive(String driveName) {
        return storeRepository.findAllByDriveNameAndRemoveType(driveName, RemoveType.SAVED)
                .stream()
                .sorted()
                .map(folder -> convertToDTO(folder, View.FolderFileView.class))
                .collect(Collectors.toList());
    }

    @Transactional
    @Override
    public void renameElement(String name, UUID link) {
        StoreAbstract element = storeRepository.findByLink(link)
                .orElseThrow(() -> new ElementNotFoundException(link));
        if (element.getName().equals(name)) {
            throw new ElementNameException(name);
        }
        element.setOldName(element.getName())
                .setName(name);
        storeRepository.save(element);
        historyService.createRenameHistory(element, name);
    }

    private void createPreRemove(StoreAbstract element, String pathInTrash) {
        PreRemove remove = new PreRemove(element, pathInTrash);
        removeRepository.save(remove);
    }

    private void preRemovePreviewImage(StoreAbstract element, Trash trash) {
        if (element instanceof File) {
            File file = (File) element;
            Map<String, LocalPath> previewOldNewPathMap = StoreUtils.preRemoveElement(file.getPreviewPath(), folderUtils, trash);
            if (!previewOldNewPathMap.isEmpty()){
                file.setPreviewPath(previewOldNewPathMap.get(ApplicationConstant.NEW_PATH));
            }
        }
    }

    private LocalPath getRestoredPath(LocalPath path) {
        return new LocalPath()
                .setFullPath(path.getOldFullPath())
                .setShortPath(path.getOldShortPath())
                .setOldFullPath(path.getFullPath())
                .setOldShortPath(path.getShortPath());
    }

    private Store convertToDTO(StoreAbstract element, Class<?> classToCheck) {
        StoreAbstractDTO storeAbstractDTO;
        if (element instanceof Folder) {
            storeAbstractDTO = folderMapper.entityToDTO((Folder) element, FolderDTO.class, classToCheck);
        } else {
            storeAbstractDTO = fileMapper.entityToDTO((File) element, FileDTO.class, classToCheck);
        }
        return storeAbstractDTO.setNoticed(StoreUtils.isStarred(element))
                .setOwner(StoreUtils.isOwner(element));
    }
}
