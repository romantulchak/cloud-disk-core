package com.romantulchak.clouddisk.service.impl;

import com.mapperDTO.mapper.EntityMapperInvoker;
import com.romantulchak.clouddisk.dto.FileDTO;
import com.romantulchak.clouddisk.dto.FolderDTO;
import com.romantulchak.clouddisk.exception.ElementAlreadyUnNoticedException;
import com.romantulchak.clouddisk.exception.ElementIsNoticedException;
import com.romantulchak.clouddisk.exception.ElementNotFoundException;
import com.romantulchak.clouddisk.model.*;
import com.romantulchak.clouddisk.repository.StarredRepository;
import com.romantulchak.clouddisk.repository.StoreRepository;
import com.romantulchak.clouddisk.service.StarredService;
import com.romantulchak.clouddisk.utils.StoreUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class StarredServiceImpl implements StarredService {

    private final StoreRepository storeRepository;
    private final StarredRepository starredRepository;
    private final EntityMapperInvoker<File, FileDTO> fileMapper;
    private final EntityMapperInvoker<Folder, FolderDTO> folderMapper;


    public StarredServiceImpl(StoreRepository storeRepository,
                              StarredRepository starredRepository,
                              EntityMapperInvoker<File, FileDTO> file,
                              EntityMapperInvoker<Folder, FolderDTO> folderMapper) {
        this.storeRepository = storeRepository;
        this.starredRepository = starredRepository;
        this.folderMapper = folderMapper;
        this.fileMapper = file;
    }

    @Override
    public List<Store> findNoticedElements(Authentication authentication) {
        UserDetailsImpl userDetails = getUserDetails(authentication);
        List<StoreAbstract> elements = storeRepository.findAllByStarredsUserId(userDetails.getId());
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
    public void addElementToNoticed(UUID link, Authentication authentication) {
        StoreAbstract element = storeRepository.findByLink(link)
                .orElseThrow(() -> new ElementNotFoundException(link));
        UserDetailsImpl userDetails = getUserDetails(authentication);
        Optional<Starred> optionalStarred = starredRepository.findByElementIdAndUserId(element.getId(), userDetails.getId());
        if (optionalStarred.isEmpty()) {
            User user = new User()
                    .setId(userDetails.getId());
            Starred starred = new Starred(user, element);
            starredRepository.save(starred);
        } else {
            throw new ElementIsNoticedException(optionalStarred.get().getElement().getName());
        }
    }

    @Override
    public void removeFromNoticed(UUID link, Authentication authentication) {
        StoreAbstract element = storeRepository.findByLink(link)
                .orElseThrow(() -> new ElementNotFoundException(link));
        UserDetailsImpl userDetails = getUserDetails(authentication);
        Optional<Starred> optionalStarred = starredRepository.findByElementIdAndUserId(element.getId(), userDetails.getId());
        if (optionalStarred.isPresent()) {
            starredRepository.delete(optionalStarred.get());
        } else {
            throw new ElementAlreadyUnNoticedException(element.getName());
        }
    }

    private UserDetailsImpl getUserDetails(Authentication authentication) {
        return (UserDetailsImpl) authentication.getPrincipal();
    }

    private FolderDTO convertToFolderDTO(Folder folder, Class<?> classToCheck) {
        return folderMapper.entityToDTO(folder, FolderDTO.class, classToCheck)
                .setNoticed(StoreUtils.isStarred(folder))
                .setOwner(StoreUtils.isOwner(folder));
    }

    private FileDTO convertToFileDTO(File file, Class<?> classToCheck) {
        return fileMapper.entityToDTO(file, FileDTO.class, classToCheck)
                .setNoticed(StoreUtils.isStarred(file))
                .setOwner(StoreUtils.isOwner(file));
    }

}
