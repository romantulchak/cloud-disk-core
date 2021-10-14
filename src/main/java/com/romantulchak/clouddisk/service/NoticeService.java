package com.romantulchak.clouddisk.service;

import com.romantulchak.clouddisk.model.Store;
import org.springframework.security.core.Authentication;

import java.util.List;
import java.util.UUID;

public interface NoticeService {

    List<Store> findNoticedElements(Authentication authentication);

    void addElementToNoticed(UUID link);

    void removeFromNoticed(UUID link);
}
