package com.romantulchak.clouddisk.controller;

import com.fasterxml.jackson.annotation.JsonView;
import com.romantulchak.clouddisk.model.Store;
import com.romantulchak.clouddisk.model.View;
import com.romantulchak.clouddisk.service.ElementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@CrossOrigin(value = "*", maxAge = 3600L)
@RequestMapping("/api/element")
public class ElementController {

    private final ElementService elementService;

    public ElementController(ElementService elementSErvice) {
        this.elementService = elementSErvice;
    }

    @PutMapping("/restore/{link}")
    @PreAuthorize("hasRole('USER') AND @userElementAccess.hasFullAccess(#link, authentication) OR @userElementAccess.hasEditAccess(#link, authentication)")
    public void restoreElement(@PathVariable("link") UUID link) {
        elementService.restoreElement(link);
    }

    @PutMapping("/pre-remove/{link}")
    @PreAuthorize("hasRole('USER') AND @userElementAccess.hasFullAccess(#link, authentication) OR @userElementAccess.hasEditAccess(#link, authentication)")
    public void preRemoveElement(@PathVariable("link") UUID link, @RequestBody String driveName) {
        elementService.preRemoveElement(link, driveName);
    }

    @DeleteMapping("/remove/{link}")
    @PreAuthorize("hasRole('USER') AND @userElementAccess.hasFullAccess(#link, authentication) OR @userElementAccess.hasEditAccess(#link, authentication)")
    public void removeElement(@PathVariable("link") UUID link) {
        elementService.removeElement(link);
    }

    @GetMapping("/removed/{driveName}")
    @PreAuthorize("hasRole('USER') AND @userDriverAccess.checkAccess(#driveName, authentication)")
    @JsonView(View.FolderFileView.class)
    public List<Store> getRemovedElements(@PathVariable("driveName") String driveName) {
        return elementService.findRemovedElements(driveName);
    }

    @GetMapping("/{driveName}")
    @PreAuthorize("hasRole('USER') AND @userDriverAccess.checkAccess(#driveName, authentication)")
    @JsonView(View.FolderFileView.class)
    public List<Store> findAllElementsForDrive(@PathVariable("driveName") String driveName) {
        return elementService.findElementsForDrive(driveName);
    }

    @PutMapping("/rename/{link}")
    @PreAuthorize("hasRole('USER') AND @userElementAccess.hasFullAccess(#link, authentication) OR @userElementAccess.hasEditAccess(#link, authentication)")
    public void renameElement(@RequestBody String name, @PathVariable("link") UUID link) {
        elementService.renameElement(name, link);
    }
}
