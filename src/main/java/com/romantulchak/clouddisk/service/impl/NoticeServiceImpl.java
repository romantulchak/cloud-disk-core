package com.romantulchak.clouddisk.service.impl;

import com.mapperDTO.mapper.EntityMapperInvoker;
import com.romantulchak.clouddisk.dto.FileDTO;
import com.romantulchak.clouddisk.dto.FolderDTO;
import com.romantulchak.clouddisk.exception.ElementAlreadyUnNoticedException;
import com.romantulchak.clouddisk.exception.ElementIsNotNoticedException;
import com.romantulchak.clouddisk.exception.ElementNotFoundException;
import com.romantulchak.clouddisk.exception.FolderNotFoundException;
import com.romantulchak.clouddisk.model.*;
import com.romantulchak.clouddisk.repository.StoreRepository;
import com.romantulchak.clouddisk.service.NoticeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class NoticeServiceImpl implements NoticeService {

    private final StoreRepository storeRepository;
    private final EntityMapperInvoker<File, FileDTO> fileMapper;
    private final EntityMapperInvoker<Folder, FolderDTO> folderMapper;

    @Autowired
    public NoticeServiceImpl(StoreRepository storeRepository, EntityMapperInvoker<File, FileDTO> file, EntityMapperInvoker<Folder, FolderDTO> folderMapper) {
        this.storeRepository = storeRepository;
        this.folderMapper = folderMapper;
        this.fileMapper = file;
    }

    @Override
    public List<Store> findNoticedElements(Authentication authentication) {
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        List<StoreAbstract> elements = storeRepository.findAllByOwnerIdAndNoticed(userDetails.getId(), true);
        List<Store> stores = new ArrayList<>();
        List<FolderDTO> folders = elements.stream().filter(Folder.class::isInstance)
                .map(Folder.class::cast)
                .map(folder -> convertToFolderDTO(folder, View.FolderFileView.class))
                .collect(Collectors.toList());
        List<FileDTO> files = elements.stream().filter(File.class::isInstance)
                .map(File.class::cast)
                .map(file -> convertToFileDTO(file, View.FolderFileView.class))
                .collect(Collectors.toList());
        stores.addAll(folders);
        stores.addAll(files);
        return stores;
    }

    @Override
    public void addElementToNoticed(UUID link) {
        StoreAbstract element = storeRepository.findByLink(link)
                .orElseThrow(() -> new ElementNotFoundException(link));
        if (element.isNoticed()) {
            throw new ElementIsNotNoticedException(element.getName());
        }
        element.setNoticed(true);
        storeRepository.save(element);
    }

    @Override
    public void removeFromNoticed(UUID link) {
        StoreAbstract element = storeRepository.findByLink(link)
                .orElseThrow(() -> new ElementNotFoundException(link));
        if (!element.isNoticed()) {
            throw new ElementAlreadyUnNoticedException(element.getName());
        }
        element.setNoticed(false);
        storeRepository.save(element);
    }

    private FolderDTO convertToFolderDTO(Folder entity, Class<?> classToCheck) {
        return this.folderMapper.entityToDTO(entity, FolderDTO.class, classToCheck);
    }

    private FileDTO convertToFileDTO(File entity, Class<?> classToCheck) {
        return this.fileMapper.entityToDTO(entity, FileDTO.class, classToCheck);
    }

}
