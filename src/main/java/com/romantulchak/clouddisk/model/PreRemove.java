package com.romantulchak.clouddisk.model;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Entity
public class PreRemove {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NotNull
    private LocalDate removeDate;

    @NotNull
    private LocalDate addedToTrash;

    @OneToOne
    @MapsId
    private StoreAbstract element;

    private String path;

    public PreRemove() {
    }

    public PreRemove(StoreAbstract element, String path) {
        this.removeDate = LocalDate.now().plusMonths(1);
        this.addedToTrash = LocalDate.now();
        this.element = element;
        this.path = path;
    }

    public LocalDate getRemoveDate() {
        return removeDate;
    }

    public void setRemoveDate(LocalDate removeDate) {
        this.removeDate = removeDate;
    }

    public LocalDate getAddedToTrash() {
        return addedToTrash;
    }

    public void setAddedToTrash(LocalDate addedToTrash) {
        this.addedToTrash = addedToTrash;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public StoreAbstract getElement() {
        return element;
    }

    public void setElement(StoreAbstract element) {
        this.element = element;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
