package com.romantulchak.clouddisk.controller;

import com.fasterxml.jackson.annotation.JsonView;
import com.romantulchak.clouddisk.model.Store;
import com.romantulchak.clouddisk.model.View;
import com.romantulchak.clouddisk.service.StarredService;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@CrossOrigin(value = "*", maxAge = 3600L)
@RequestMapping("/api/notice")
public class NoticeController {

    private final StarredService starredService;

    public NoticeController(StarredService starredService) {
        this.starredService = starredService;
    }

    @GetMapping("/get-noticed-elements")
    @JsonView(View.FolderFileView.class)
    public List<Store> getNoticedElements(Authentication authentication) {
        return starredService.findNoticedElements(authentication);
    }

    @PutMapping("/set-noticed/{link}")
    public void setElementNoticed(@PathVariable("link") UUID link, Authentication authentication) {
        starredService.addElementToNoticed(link, authentication);
    }

    @PutMapping("/remove-from-noticed/{link}")
    public void removeFromNoticed(@PathVariable("link") UUID link, Authentication authentication){
        starredService.removeFromNoticed(link, authentication);
    }
}
