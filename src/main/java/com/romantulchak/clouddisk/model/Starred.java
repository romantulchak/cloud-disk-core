package com.romantulchak.clouddisk.model;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
public class Starred {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    private User user;

    @ManyToOne
    private StoreAbstract element;

    private LocalDateTime added;

    public Starred() {
    }

    public Starred(User user, StoreAbstract element) {
        this.user = user;
        this.element = element;
        this.added = LocalDateTime.now();
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public StoreAbstract getElement() {
        return element;
    }

    public void setElement(StoreAbstract element) {
        this.element = element;
    }

    public LocalDateTime getAdded() {
        return added;
    }

    public void setAdded(LocalDateTime added) {
        this.added = added;
    }
}
