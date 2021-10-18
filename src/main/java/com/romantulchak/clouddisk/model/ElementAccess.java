package com.romantulchak.clouddisk.model;

import com.romantulchak.clouddisk.model.enums.StoreAccessType;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
public class ElementAccess {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @OneToOne
    private StoreAbstract element;

    private LocalDateTime createAt;

    @Enumerated(EnumType.STRING)
    private StoreAccessType accessType;

    public ElementAccess() {
        this.createAt = LocalDateTime.now();
    }

    public long getId() {
        return id;
    }

    public ElementAccess setId(long id) {
        this.id = id;
        return this;
    }

    public StoreAbstract getElement() {
        return element;
    }

    public ElementAccess setElement(StoreAbstract element) {
        this.element = element;
        return this;
    }

    public LocalDateTime getCreateAt() {
        return createAt;
    }

    public ElementAccess setCreateAt(LocalDateTime createAt) {
        this.createAt = createAt;
        return this;
    }

    public StoreAccessType getAccessType() {
        return accessType;
    }

    public ElementAccess setAccessType(String accessType) {
        boolean isAccessTypeExists = StoreAccessType.findAccessType(accessType);
        if (isAccessTypeExists){
            this.accessType = StoreAccessType.valueOf(accessType);
        }else{
            this.accessType = StoreAccessType.READER;
        }
        return this;
    }
}
