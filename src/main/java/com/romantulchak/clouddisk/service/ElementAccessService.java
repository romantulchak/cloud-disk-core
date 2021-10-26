package com.romantulchak.clouddisk.service;

import com.romantulchak.clouddisk.dto.ElementAccessDTO;
import com.romantulchak.clouddisk.dto.StoreAccessDTO;

import java.util.List;
import java.util.UUID;

public interface ElementAccessService {

    ElementAccessDTO findElementAccess(UUID uuid);

    List<StoreAccessDTO> getAccessTypes();

    ElementAccessDTO openAccess(UUID link, String type);

    ElementAccessDTO changeAccess(UUID link, String type);

    void closeAccess(UUID link);
}
