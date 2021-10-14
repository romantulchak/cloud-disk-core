package com.romantulchak.clouddisk.service.impl;

import com.mapperDTO.mapper.EntityMapperInvoker;
import com.romantulchak.clouddisk.dto.StoreAbstractDTO;
import com.romantulchak.clouddisk.model.File;
import com.romantulchak.clouddisk.model.StoreAbstract;
import com.romantulchak.clouddisk.model.View;
import com.romantulchak.clouddisk.model.enums.ContextType;
import com.romantulchak.clouddisk.repository.StoreRepository;
import com.romantulchak.clouddisk.service.SearchService;
import com.romantulchak.clouddisk.utils.StoreUtils;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SearchServiceImpl implements SearchService {

    private final StoreRepository storeRepository;
    private final EntityMapperInvoker<StoreAbstract, StoreAbstractDTO> entityMapperInvoker;

    public SearchServiceImpl(StoreRepository storeRepository,
                             EntityMapperInvoker<StoreAbstract, StoreAbstractDTO> entityMapperInvoker) {
        this.storeRepository = storeRepository;
        this.entityMapperInvoker = entityMapperInvoker;
    }

    @Override
    public List<StoreAbstractDTO> search(String name, Authentication authentication) {
        UserDetailsImpl principal = (UserDetailsImpl) authentication.getPrincipal();
        return storeRepository.findTop5ByNameIsContainingIgnoreCaseAndOwnerId(name, principal.getId())
                .stream()
                .map(storeAbstract -> convertToDTO(storeAbstract, View.FolderFileView.class))
                .collect(Collectors.toList());
    }

    private StoreAbstractDTO convertToDTO(StoreAbstract entity, Class<?> classToCheck) {
        StoreAbstractDTO store = entityMapperInvoker.entityToDTO(entity, StoreAbstractDTO.class, classToCheck);
        StoreUtils.setContext(entity, store);
        return store;
    }
}
