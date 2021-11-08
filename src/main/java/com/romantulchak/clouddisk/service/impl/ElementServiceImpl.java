package com.romantulchak.clouddisk.service.impl;

import com.mapperDTO.mapper.EntityMapperInvoker;
import com.romantulchak.clouddisk.dto.FileDTO;
import com.romantulchak.clouddisk.dto.FolderDTO;
import com.romantulchak.clouddisk.dto.StoreAbstractDTO;
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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class ElementServiceImpl implements ElementService {

    private final StoreRepository storeRepository;
    private final FolderUtils folderUtils;
    private final PreRemoveRepository removeRepository;
    private final TrashService trashService;
    private final EntityMapperInvoker<Folder, FolderDTO> folderMapper;
    private final EntityMapperInvoker<File, FileDTO> fileMapper;

    public ElementServiceImpl(StoreRepository storeRepository,
                              FolderUtils folderUtils,
                              PreRemoveRepository removeRepository,
                              TrashService trashService,
                              EntityMapperInvoker<Folder, FolderDTO> folderMapper,
                              EntityMapperInvoker<File, FileDTO> fileMapper) {
        this.storeRepository = storeRepository;
        this.folderUtils = folderUtils;
        this.removeRepository = removeRepository;
        this.trashService = trashService;
        this.folderMapper = folderMapper;
        this.fileMapper = fileMapper;
    }

    @Transactional
    @Override
    public void restoreElement(UUID elementLink) {
        StoreAbstract element = storeRepository.findByLink(elementLink)
                .orElseThrow(() -> new FileNotFoundException(elementLink));
        if (element.getRemoveType() == RemoveType.PRE_REMOVED) {
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
            if (isMoved) {
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

    private void createPreRemove(StoreAbstract element, String pathInTrash) {
        PreRemove remove = new PreRemove(element, pathInTrash);
        removeRepository.save(remove);
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
