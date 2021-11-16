package com.romantulchak.clouddisk.service.impl;

import com.mapperDTO.mapper.EntityMapperInvoker;
import com.romantulchak.clouddisk.dto.HistoryDTO;
import com.romantulchak.clouddisk.model.History;
import com.romantulchak.clouddisk.model.StoreAbstract;
import com.romantulchak.clouddisk.model.User;
import com.romantulchak.clouddisk.model.View;
import com.romantulchak.clouddisk.model.enums.HistoryType;
import com.romantulchak.clouddisk.repository.HistoryRepository;
import com.romantulchak.clouddisk.service.HistoryService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class HistoryServiceImpl implements HistoryService {

    private final HistoryRepository historyRepository;
    private final EntityMapperInvoker<History, HistoryDTO> entityMapperInvoker;

    public HistoryServiceImpl(HistoryRepository historyRepository,
                              EntityMapperInvoker<History, HistoryDTO> entityMapperInvoker) {
        this.historyRepository = historyRepository;
        this.entityMapperInvoker = entityMapperInvoker;
    }

    @Override
    public History create(StoreAbstract element, HistoryType type) {
        Authentication authentication = SecurityContextHolder
                .getContext()
                .getAuthentication();
        UserDetailsImpl principal = (UserDetailsImpl) authentication.getPrincipal();
        User user = new User()
                .setId(principal.getId());
        History history = new History(element, type, user);
        return historyRepository.save(history);
    }

    @Override
    public List<HistoryDTO> findHistoryForElement(long id) {
        return historyRepository.findAllByElementId(id)
                .stream()
                .map(history -> convertToDTO(history, View.HistoryView.class))
                .collect(Collectors.toList());
    }

    private HistoryDTO convertToDTO(History history, Class<?> classToCheck){
        return entityMapperInvoker.entityToDTO(history, HistoryDTO.class, classToCheck);
    }
}
