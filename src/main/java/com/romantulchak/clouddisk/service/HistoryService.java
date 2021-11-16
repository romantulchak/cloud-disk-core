package com.romantulchak.clouddisk.service;

import com.romantulchak.clouddisk.dto.HistoryDTO;
import com.romantulchak.clouddisk.model.History;
import com.romantulchak.clouddisk.model.StoreAbstract;
import com.romantulchak.clouddisk.model.enums.HistoryType;

import java.util.List;

public interface HistoryService {

    History create(StoreAbstract element, HistoryType type);

    List<HistoryDTO> findHistoryForElement(long id);

}
