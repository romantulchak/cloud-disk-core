package com.romantulchak.clouddisk.model;

import com.romantulchak.clouddisk.model.enums.RoleType;

import javax.persistence.*;
import javax.validation.constraints.Size;

@Entity
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Size(max = 90)
    @Enumerated(EnumType.STRING)
    private RoleType name;

    public Role(){

    }

    public Role(RoleType name) {
        this.name = name;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public RoleType getName() {
        return name;
    }

    public void setName(RoleType name) {
        this.name = name;
    }
}
