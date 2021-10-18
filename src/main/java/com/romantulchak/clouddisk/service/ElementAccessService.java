package com.romantulchak.clouddisk.service;

import com.romantulchak.clouddisk.dto.ElementAccessDTO;
import com.romantulchak.clouddisk.dto.StoreAccessDTO;

import java.util.List;
import java.util.UUID;

public interface ElementAccessService {

    ElementAccessDTO findElementAccess(UUID uuid, String accessType);

    List<StoreAccessDTO> getAccessTypes();

    void changeAccess(UUID link, String type);
}
