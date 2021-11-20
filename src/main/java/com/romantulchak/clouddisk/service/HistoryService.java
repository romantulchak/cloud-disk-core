package com.romantulchak.clouddisk.service;

import com.romantulchak.clouddisk.dto.HistoryDTO;
import com.romantulchak.clouddisk.model.history.History;
import com.romantulchak.clouddisk.model.StoreAbstract;
import com.romantulchak.clouddisk.model.enums.HistoryType;

import java.util.List;

public interface HistoryService {

    History create(StoreAbstract element, HistoryType type);

    History createRenameHistory(StoreAbstract element, String name);

    List<HistoryDTO> findHistoryForElement(long id);

}
