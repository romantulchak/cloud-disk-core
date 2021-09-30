package com.romantulchak.clouddisk.model;

import com.romantulchak.clouddisk.model.enums.PlanType;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.util.List;

@Entity
public class Plan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Size(max = 90)
    @Enumerated(EnumType.STRING)
    private PlanType name;

    @OneToMany(mappedBy = "plan")
    private List<Drive> drives;


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public PlanType getName() {
        return name;
    }

    public void setName(PlanType name) {
        this.name = name;
    }

    public List<Drive> getDrives() {
        return drives;
    }

    public void setDrives(List<Drive> drives) {
        this.drives = drives;
    }
}
