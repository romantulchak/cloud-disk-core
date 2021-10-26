package com.romantulchak.clouddisk.access;

import com.romantulchak.clouddisk.exception.ElementNotFoundException;
import com.romantulchak.clouddisk.model.StoreAbstract;
import com.romantulchak.clouddisk.model.enums.StoreAccessType;
import com.romantulchak.clouddisk.repository.StoreRepository;
import com.romantulchak.clouddisk.service.impl.UserDetailsImpl;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class UserElementAccess {

    private final StoreRepository storeRepository;

    public UserElementAccess(StoreRepository storeRepository){
        this.storeRepository = storeRepository;
    }

    public boolean hasReadAccess(UUID link, Authentication authentication){
        return checkAccess(link, authentication, StoreAccessType.READER);
    }

    public boolean hasCommentAccess(UUID link, Authentication authentication){
        return checkAccess(link, authentication, StoreAccessType.EDITOR);
    }

    public boolean hasEditAccess(UUID link, Authentication authentication){
        return checkAccess(link, authentication, StoreAccessType.EDITOR);
    }

    public boolean hasFullAccess(UUID link, Authentication authentication){
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        return storeRepository.existsByLinkAndOwnerId(link, userDetails.getId());
    }

    private boolean checkAccess(UUID link, Authentication authentication, StoreAccessType type) {
        StoreAbstract element = storeRepository.findByLink(link)
                .orElseThrow(() -> new ElementNotFoundException(link));
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        boolean isUserFolder = userDetails.getId() == element.getOwner().getId();
        if (element.getAccess() == null && !isUserFolder){
            return false;
        }else{
            return isUserFolder || element.getAccess().getAccessType() == type;
        }
    }

}
