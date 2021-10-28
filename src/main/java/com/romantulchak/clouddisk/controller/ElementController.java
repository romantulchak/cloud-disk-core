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

}
