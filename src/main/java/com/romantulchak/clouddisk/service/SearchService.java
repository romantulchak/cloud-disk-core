package com.romantulchak.clouddisk.service;

import com.romantulchak.clouddisk.dto.StoreAbstractDTO;
import org.springframework.security.core.Authentication;

import java.util.List;


public interface SearchService {

    List<StoreAbstractDTO> search(String name, Authentication authentication);

}
