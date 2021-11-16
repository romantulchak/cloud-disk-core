package com.romantulchak.clouddisk.controller;

import com.fasterxml.jackson.annotation.JsonView;
import com.romantulchak.clouddisk.dto.HistoryDTO;
import com.romantulchak.clouddisk.model.View;
import com.romantulchak.clouddisk.service.HistoryService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(value = "*", maxAge = 3600L)
@RequestMapping("/api/history")
public class HistoryController {

    private final HistoryService historyService;

    public HistoryController(HistoryService historyService) {
        this.historyService = historyService;
    }

    @GetMapping("/{id}")
    @JsonView(View.HistoryView.class)
    public List<HistoryDTO> getHistoryForElement(@PathVariable("id") long id){
        return historyService.findHistoryForElement(id);
    }
}
