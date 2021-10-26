package com.romantulchak.clouddisk.controller;

import com.fasterxml.jackson.annotation.JsonView;
import com.romantulchak.clouddisk.dto.ElementAccessDTO;
import com.romantulchak.clouddisk.dto.StoreAccessDTO;
import com.romantulchak.clouddisk.model.View;
import com.romantulchak.clouddisk.service.ElementAccessService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@CrossOrigin(value = "*", maxAge = 3600L)
@RequestMapping("/api/access")
public class ElementAccessController {

    private final ElementAccessService elementAccessService;

    public ElementAccessController(ElementAccessService elementAccessService) {
        this.elementAccessService = elementAccessService;
    }

    @GetMapping("/{link}")
    @JsonView(View.ElementAccessView.class)
    public ElementAccessDTO findElementAccess(@PathVariable("link") UUID link) {
        return elementAccessService.findElementAccess(link);
    }

    @PutMapping("/open/{link}")
    @JsonView(View.ElementAccessView.class)
    @PreAuthorize("@userElementAccess.hasFullAccess(#link, authentication) OR @userElementAccess.hasEditAccess(#link, authentication)")
    public ElementAccessDTO openAccess(@RequestBody String type, @PathVariable("link") UUID link){
        return elementAccessService.openAccess(link, type);
    }

    @PutMapping("/close/{link}")
    @PreAuthorize("@userElementAccess.hasFullAccess(#link, authentication) OR @userElementAccess.hasEditAccess(#link, authentication)")
    public void closeAccess(@PathVariable("link") UUID link){
        elementAccessService.closeAccess(link);
    }

    @PutMapping("/change/{link}")
    public ElementAccessDTO changeAccessType(@PathVariable("link") UUID link,
                                 @RequestBody String type) {
       return elementAccessService.changeAccess(link, type);
    }

    @GetMapping("/types")
    public List<StoreAccessDTO> getAccessTypes() {
        return elementAccessService.getAccessTypes();
    }

}
