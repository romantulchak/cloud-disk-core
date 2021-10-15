package com.romantulchak.clouddisk.service.impl;

import com.mapperDTO.mapper.EntityMapperInvoker;
import com.romantulchak.clouddisk.dto.ElementAccessDTO;
import com.romantulchak.clouddisk.exception.ElementNotFoundException;
import com.romantulchak.clouddisk.model.ElementAccess;
import com.romantulchak.clouddisk.model.StoreAbstract;
import com.romantulchak.clouddisk.model.View;
import com.romantulchak.clouddisk.model.enums.StoreAccessType;
import com.romantulchak.clouddisk.repository.ElementAccessRepository;
import com.romantulchak.clouddisk.repository.StoreRepository;
import com.romantulchak.clouddisk.service.ElementAccessService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Service
public class ElementAccessServiceImpl implements ElementAccessService {

    private final StoreRepository storeRepository;
    private final ElementAccessRepository elementAccessRepository;
    private final EntityMapperInvoker<ElementAccess, ElementAccessDTO> entityMapperInvoker;

    public ElementAccessServiceImpl(StoreRepository storeRepository,
                                    ElementAccessRepository elementAccessRepository,
                                    EntityMapperInvoker<ElementAccess, ElementAccessDTO> entityMapperInvoker) {
        this.storeRepository = storeRepository;
        this.elementAccessRepository = elementAccessRepository;
        this.entityMapperInvoker = entityMapperInvoker;
    }

    @Override
    public ElementAccessDTO findElementAccess(UUID link, String type) {
        Optional<ElementAccess> optionalElementAccess = elementAccessRepository.findByElementLink(link);
        if (optionalElementAccess.isPresent()){
            return convertToDTO(optionalElementAccess.get(), View.ElementAccessView.class);
        }
        return openAccessByLink(link, type);
    }

    @Transactional
    @Override
    public ElementAccessDTO openAccessByLink(UUID link, String type) {
        StoreAbstract element = storeRepository.findByLink(link)
                .orElseThrow(() -> new ElementNotFoundException(link));
        ElementAccess elementAccess = new ElementAccess()
                .setElement(element)
                .setAccessType(type);
        elementAccess = elementAccessRepository.save(elementAccess);
        openAccessToElement(element, elementAccess);
        return convertToDTO(elementAccess, View.ElementAccessView.class);
    }

    private void openAccessToElement(StoreAbstract element, ElementAccess elementAccess){
        element.setHasLinkAccess(true);
        element.setAccess(elementAccess);
        storeRepository.save(element);
    }

    private ElementAccessDTO convertToDTO(ElementAccess elementAccess, Class<?> classToCheck){
        return entityMapperInvoker.entityToDTO(elementAccess, ElementAccessDTO.class, classToCheck);
    }
}
