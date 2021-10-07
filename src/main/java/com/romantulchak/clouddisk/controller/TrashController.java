package com.romantulchak.clouddisk.controller;

import com.fasterxml.jackson.annotation.JsonView;
import com.romantulchak.clouddisk.model.Store;
import com.romantulchak.clouddisk.model.View;
import com.romantulchak.clouddisk.service.TrashService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(value = "*", maxAge = 3600L)
@RequestMapping("/api/trash")
public class TrashController {

    private final TrashService trashService;

    @Autowired
    public TrashController(TrashService trashService) {
        this.trashService = trashService;
    }

    @GetMapping("/removed-elements/{driveName}")
    @PreAuthorize("hasRole('USER') AND @userDriverAccess.checkAccess(#driveName, authentication)")
    @JsonView(View.FolderFileView.class)
    public List<Store> getRemovedElements(@PathVariable("driveName") String driveName){
        return trashService.getRemovedElements(driveName);
    }

}
