package com.romantulchak.clouddisk.model;

import com.romantulchak.clouddisk.model.enums.HistoryType;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
public class History {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    private StoreAbstract element;

    @Enumerated(EnumType.STRING)
    private HistoryType type;

    @ManyToOne
    private User user;

    private LocalDateTime date;

    public History() {
    }

    public History(StoreAbstract element, HistoryType type, User user) {
        this.element = element;
        this.type = type;
        this.user = user;
        this.date = LocalDateTime.now();
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

    public HistoryType getType() {
        return type;
    }

    public void setType(HistoryType type) {
        this.type = type;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }
}
