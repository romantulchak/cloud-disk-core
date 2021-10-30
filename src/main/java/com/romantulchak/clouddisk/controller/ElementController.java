package com.romantulchak.clouddisk.controller;

import com.romantulchak.clouddisk.service.ElementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@CrossOrigin(value = "*", maxAge = 3600L)
@RequestMapping("/api/element")
public class ElementController {

    private final ElementService elementService;

    @Autowired
    public ElementController(ElementService elementSErvice) {
        this.elementService = elementSErvice;
    }

    @PutMapping("/restore/{link}")
    @PreAuthorize("hasRole('USER') AND @userElementAccess.hasFullAccess(#link, authentication)")
    public void restoreElement(@PathVariable("link")UUID link){
        elementService.restoreElement(link);
    }


    @PutMapping("/pre-remove/{link}")
    @PreAuthorize("hasRole('USER') AND @userFolderAccess.isAccessToSubFolder(#link, authentication)")
    public void preRemoveElement(@PathVariable("link") UUID link, @RequestBody String driveName){
        elementService.preRemoveElement(link, driveName);
    }

    @DeleteMapping("/remove/{link}")
    @PreAuthorize("hasRole('USER') AND @userFolderAccess.isAccessToSubFolder(#link, authentication)")
    public void removeElement(@PathVariable("link") UUID link){
        elementService.removeElement(link);
    }

}
