package com.romantulchak.clouddisk.service;

import com.romantulchak.clouddisk.dto.HistoryDTO;
import com.romantulchak.clouddisk.model.enums.ContextType;
import com.romantulchak.clouddisk.model.history.History;
import com.romantulchak.clouddisk.model.StoreAbstract;
import com.romantulchak.clouddisk.model.enums.HistoryType;
import org.springframework.security.core.Authentication;

import java.util.List;
import java.util.UUID;

public interface HistoryService {

    History create(StoreAbstract element, HistoryType type);

    History createRenameHistory(StoreAbstract element, String name);

    History createUploadHistory(StoreAbstract element, String name, UUID link, String fullPath, ContextType context, Authentication authentication);

    List<HistoryDTO> findHistoryForElement(long id);

}