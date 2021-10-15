package com.romantulchak.clouddisk.controller;

import com.fasterxml.jackson.annotation.JsonView;
import com.romantulchak.clouddisk.dto.ElementAccessDTO;
import com.romantulchak.clouddisk.model.View;
import com.romantulchak.clouddisk.service.ElementAccessService;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.UUID;

@RestController
@CrossOrigin(value = "*", maxAge = 3600L)
@RequestMapping("/api/access")
public class ElementAccessController {

    private final ElementAccessService elementAccessService;

    public ElementAccessController(ElementAccessService elementAccessService) {
        this.elementAccessService = elementAccessService;
    }

    @GetMapping("/{link}/{type}")
    @JsonView(View.ElementAccessView.class)
    public ElementAccessDTO findElementAccess(@PathVariable("link") UUID link,
                                              @PathVariable("type") String type) {
        return elementAccessService.findElementAccess(link, type);
    }

}
