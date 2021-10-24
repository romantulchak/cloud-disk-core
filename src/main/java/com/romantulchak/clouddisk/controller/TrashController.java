package com.romantulchak.clouddisk.controller;

import com.romantulchak.clouddisk.service.TrashService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin(value = "*", maxAge = 3600L)
@RequestMapping("/api/trash")
public class TrashController {

    private final TrashService trashService;

    @Autowired
    public TrashController(TrashService trashService) {
        this.trashService = trashService;
    }

}
