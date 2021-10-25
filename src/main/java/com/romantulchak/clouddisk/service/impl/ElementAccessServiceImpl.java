package com.romantulchak.clouddisk.service.impl;

import com.mapperDTO.mapper.EntityMapperInvoker;
import com.romantulchak.clouddisk.dto.ElementAccessDTO;
import com.romantulchak.clouddisk.dto.StoreAccessDTO;
import com.romantulchak.clouddisk.exception.ElementNotFoundException;
import com.romantulchak.clouddisk.model.*;
import com.romantulchak.clouddisk.model.enums.StoreAccessType;
import com.romantulchak.clouddisk.repository.ElementAccessRepository;
import com.romantulchak.clouddisk.repository.FileRepository;
import com.romantulchak.clouddisk.repository.FolderRepository;
import com.romantulchak.clouddisk.repository.StoreRepository;
import com.romantulchak.clouddisk.service.ElementAccessService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class ElementAccessServiceImpl implements ElementAccessService {

    private final StoreRepository storeRepository;
    private final FolderRepository folderRepository;
    private final FileRepository fileRepository;
    private final ElementAccessRepository elementAccessRepository;
    private final EntityMapperInvoker<ElementAccess, ElementAccessDTO> entityMapperInvoker;

    public ElementAccessServiceImpl(StoreRepository storeRepository,
                                    ElementAccessRepository elementAccessRepository,
                                    FolderRepository folderRepository,
                                    FileRepository fileRepository,
                                    EntityMapperInvoker<ElementAccess, ElementAccessDTO> entityMapperInvoker) {
        this.storeRepository = storeRepository;
        this.elementAccessRepository = elementAccessRepository;
        this.folderRepository = folderRepository;
        this.fileRepository = fileRepository;
        this.entityMapperInvoker = entityMapperInvoker;
    }

    @Transactional
    @Override
    public ElementAccessDTO findElementAccess(UUID link) {
        Optional<ElementAccess> optionalElementAccess = elementAccessRepository.findByElementLink(link);
        return optionalElementAccess.
                map(elementAccess -> convertToDTO(elementAccess, View.ElementAccessView.class))
                .orElse(null);
    }

    @Override
    public ElementAccessDTO openAccess(UUID link, String type) {
        StoreAbstract element = storeRepository.findByLink(link)
                .orElseThrow(() -> new ElementNotFoundException(link));
        ElementAccess elementAccess = new ElementAccess()
                .setElement(element)
                .setAccessType(type);
        elementAccess = elementAccessRepository.save(elementAccess);
        if (element instanceof Folder) {
            setAccessToSubElements((Folder) element, type);
        }
        openAccessToElement(element);
        return convertToDTO(elementAccess, View.ElementAccessView.class);
    }

    @Override
    public List<StoreAccessDTO> getAccessTypes() {
        return StoreAccessType.getAccessTypes();
    }

    @Override
    public ElementAccessDTO changeAccess(UUID link, String type) {
        StoreAbstract element = storeRepository.findByLink(link)
                .orElseThrow(() -> new ElementNotFoundException(link));
        ElementAccessDTO elementAccess = convertToDTO(element.getAccess(), View.ElementAccessView.class);
        if (element.getAccess() != null) {
            updateAccess(element, type);
            if (element instanceof Folder) {
                setAccessToSubElements((Folder) element, type);
            }
        } else {
            elementAccess = openAccess(link, type);
        }
        return elementAccess;
    }

    private void setAccessToSubElements(Folder subFolder, String type) {
        List<Folder> subFolders = folderRepository.findByLink(subFolder.getLink()).getSubFolders();
        List<File> files = fileRepository.findAllByFolderLink(subFolder.getLink());
        for (File file : files) {
            openAccessToElement(file);
            updateAccess(file, type);
        }
        for (Folder folder : subFolders) {
            openAccessToElement(folder);
            updateAccess(folder, type);
            setAccessToSubElements(folder, type);
        }
    }

    private void updateAccess(StoreAbstract element, String type) {
        Optional<ElementAccess> optionalElementAccess = elementAccessRepository.findByElementId(element.getId());
        ElementAccess elementAccess;
        if(optionalElementAccess.isPresent()){
            elementAccess = optionalElementAccess.get();
            elementAccess.setAccessType(type);
        } else {
            elementAccess = new ElementAccess()
                    .setElement(element)
                    .setAccessType(type);
        }
        elementAccessRepository.save(elementAccess);
    }

    private void openAccessToElement(StoreAbstract element) {
        element.setHasLinkAccess(true);
        storeRepository.save(element);
    }

    private ElementAccessDTO convertToDTO(ElementAccess elementAccess, Class<?> classToCheck) {
        return entityMapperInvoker.entityToDTO(elementAccess, ElementAccessDTO.class, classToCheck);
    }
}
