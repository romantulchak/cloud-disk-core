package com.romantulchak.clouddisk.service;

import com.romantulchak.clouddisk.dto.ElementAccessDTO;

import java.util.UUID;

public interface ElementAccessService {

    ElementAccessDTO findElementAccess(UUID uuid, String accessType);

    ElementAccessDTO openAccessByLink(UUID link, String type);

}
