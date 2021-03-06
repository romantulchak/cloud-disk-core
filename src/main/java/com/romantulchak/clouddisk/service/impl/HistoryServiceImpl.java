package com.romantulchak.clouddisk.service.impl;

import com.mapperDTO.mapper.EntityMapperInvoker;
import com.romantulchak.clouddisk.dto.HistoryDTO;
import com.romantulchak.clouddisk.dto.RenameHistoryDTO;
import com.romantulchak.clouddisk.dto.UploadHistoryDTO;
import com.romantulchak.clouddisk.model.StoreAbstract;
import com.romantulchak.clouddisk.model.User;
import com.romantulchak.clouddisk.model.View;
import com.romantulchak.clouddisk.model.enums.ContextType;
import com.romantulchak.clouddisk.model.enums.HistoryType;
import com.romantulchak.clouddisk.model.history.History;
import com.romantulchak.clouddisk.model.history.RenameHistory;
import com.romantulchak.clouddisk.model.history.UploadHistory;
import com.romantulchak.clouddisk.repository.HistoryRepository;
import com.romantulchak.clouddisk.service.HistoryService;
import com.romantulchak.clouddisk.utils.StoreUtils;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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
        User user = getAuthenticatedUser();
        History history = new History(element, type, user);
        return historyRepository.save(history);
    }

    @Override
    public History createRenameHistory(StoreAbstract element, String name) {
        User user = getAuthenticatedUser();
        History history = new RenameHistory(element, user, name);
        return historyRepository.save(history);
    }

    @Override
    public History createUploadHistory(StoreAbstract element, StoreAbstract uploadedElement, HistoryType historyType, ContextType context, Authentication authentication) {
        User user;
        if (authentication == null) {
            user = getAuthenticatedUser();
        } else {
            user = getAuthenticatedUser(authentication);
        }
        History history = new UploadHistory(element, historyType, user, uploadedElement.getName(), uploadedElement.getLink(),
                uploadedElement.getPath().getFullPath(), context);
        return historyRepository.save(history);
    }

    @Override
    public List<HistoryDTO> findHistoryForElement(long id, String page) {
        Pageable pageable = PageRequest.of(StoreUtils.parseIf(page), 10);
//        Slice<History> allByElementIdOrderByDateDesc = historyRepository.findAllByElementIdOrderByDateDesc(id, pageable);
//        List<HistoryDTO> collect = allByElementIdOrderByDateDesc.getContent().stream().map(history -> convertToDTO(history, View.HistoryView.class)).collect(Collectors.toList());

        return historyRepository.findAllByElementIdOrderByDateDesc(id, pageable)
                .getContent()
                .stream()
                .map(history -> convertToDTO(history, View.HistoryView.class))
                .collect(Collectors.toList());
    }

    private User getAuthenticatedUser() {
        Authentication authentication = SecurityContextHolder
                .getContext()
                .getAuthentication();
        UserDetailsImpl principal = (UserDetailsImpl) authentication.getPrincipal();
        return new User()
                .setId(principal.getId());
    }

    private User getAuthenticatedUser(Authentication authentication) {
        UserDetailsImpl principal = (UserDetailsImpl) authentication.getPrincipal();
        return new User()
                .setId(principal.getId());
    }

    private HistoryDTO convertToDTO(History history, Class<?> classToCheck) {
        HistoryDTO historyDTO;
        if (history instanceof RenameHistory) {
            historyDTO = entityMapperInvoker.entityToDTO(history, RenameHistoryDTO.class, classToCheck);
        } else if (history instanceof UploadHistory) {
            historyDTO = entityMapperInvoker.entityToDTO(history, UploadHistoryDTO.class, classToCheck);
        } else {
            historyDTO = entityMapperInvoker.entityToDTO(history, HistoryDTO.class, classToCheck);
        }
        historyDTO.getElement()
                .setOwner(StoreUtils.isOwner(history.getElement()));
        StoreUtils.setContext(history.getElement(), historyDTO.getElement());
        return historyDTO;
    }
}
