package com.romantulchak.clouddisk.controller;

import com.fasterxml.jackson.annotation.JsonView;
import com.romantulchak.clouddisk.dto.DriveDTO;
import com.romantulchak.clouddisk.model.View;
import com.romantulchak.clouddisk.service.DriveService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(value = "*", maxAge = 3600L)
@RequestMapping("/api/my-drive")
public class DriveController {

    private final DriveService driveService;

    public DriveController(DriveService driveService){
        this.driveService = driveService;
    }

    @GetMapping
    @PreAuthorize("hasRole('USER')")
    @JsonView(View.DriveView.class)
    public DriveDTO getUserDrive(Authentication authentication){
        return driveService.findUserDrive(authentication);
    }
}
