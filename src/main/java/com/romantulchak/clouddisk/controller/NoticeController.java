package com.romantulchak.clouddisk.controller;

import com.fasterxml.jackson.annotation.JsonView;
import com.romantulchak.clouddisk.model.Store;
import com.romantulchak.clouddisk.model.View;
import com.romantulchak.clouddisk.service.NoticeService;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@CrossOrigin(value = "*", maxAge = 3600L)
@RequestMapping("/api/notice")
public class NoticeController {

    private final NoticeService noticeService;

    public NoticeController(NoticeService noticeService) {
        this.noticeService = noticeService;
    }

    @GetMapping("/get-noticed-elements")
    @JsonView(View.FolderFileView.class)
    public List<Store> getNoticedElements(Authentication authentication) {
        return noticeService.findNoticedElements(authentication);
    }

    @PutMapping("/set-noticed/{link}")
    public void setElementNoticed(@PathVariable("link") UUID link) {
        noticeService.addElementToNoticed(link);
    }

    @PutMapping("/remove-from-noticed/{link}")
    public void removeFromNoticed(@PathVariable("link") UUID link){
        noticeService.removeFromNoticed(link);
    }
}
