package com.romantulchak.clouddisk.dto;

import com.fasterxml.jackson.annotation.JsonView;
import com.mapperDTO.annotation.DTO;
import com.mapperDTO.annotation.MapToDTO;
import com.romantulchak.clouddisk.model.View;
import com.romantulchak.clouddisk.model.enums.HistoryType;

import java.time.LocalDateTime;

@DTO
public class HistoryDTO {

    @MapToDTO(mapClass = {View.HistoryView.class})
    @JsonView(View.HistoryView.class)
    private long id;

    @MapToDTO(mapClass = {View.HistoryView.class})
    @JsonView(View.HistoryView.class)
    private StoreAbstractDTO element;

    @MapToDTO(mapClass = {View.HistoryView.class})
    @JsonView(View.HistoryView.class)
    private HistoryType type;

    @MapToDTO(mapClass = {View.HistoryView.class})
    @JsonView(View.HistoryView.class)
    private UserDTO user;

    @MapToDTO(mapClass = {View.HistoryView.class})
    @JsonView(View.HistoryView.class)
    private LocalDateTime date;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public StoreAbstractDTO getElement() {
        return element;
    }

    public void setElement(StoreAbstractDTO element) {
        this.element = element;
    }

    public HistoryType getType() {
        return type;
    }

    public void setType(HistoryType type) {
        this.type = type;
    }

    public UserDTO getUser() {
        return user;
    }

    public void setUser(UserDTO user) {
        this.user = user;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }
}
