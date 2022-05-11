package com.romantulchak.clouddisk.service;

import com.romantulchak.clouddisk.dto.HistoryDTO;
import com.romantulchak.clouddisk.model.enums.ContextType;
import com.romantulchak.clouddisk.model.history.History;
import com.romantulchak.clouddisk.model.StoreAbstract;
import com.romantulchak.clouddisk.model.enums.HistoryType;
import org.springframework.security.core.Authentication;

import java.util.List;

public interface HistoryService {

    History create(StoreAbstract element, HistoryType type);

    History createRenameHistory(StoreAbstract element, String name);

    History createUploadHistory(StoreAbstract element, StoreAbstract uploadedElement, HistoryType historyType, ContextType context, Authentication authentication);

    List<HistoryDTO> findHistoryForElement(long id, String page);

}
