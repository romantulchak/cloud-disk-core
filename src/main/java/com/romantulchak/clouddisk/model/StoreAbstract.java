package com.romantulchak.clouddisk.model;

import com.romantulchak.clouddisk.model.enums.RemoveType;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@DiscriminatorColumn(name = "store_type")
public abstract class StoreAbstract {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Size(max = 195)
    private String name;

    private LocalDateTime createAt;

    private LocalDateTime uploadAt;

    @NotNull
    @ManyToOne
    private Trash trash;

    @Embedded
    private LocalPath path;

    @Enumerated(EnumType.STRING)
    private RemoveType removeType = RemoveType.SAVED;

    @ManyToOne
    private Drive drive;

    private UUID link;

    private boolean hasLinkAccess;

    @ManyToOne
    private User owner;

    @OneToOne(mappedBy = "element")
    private PreRemove preRemove;

    public long getId() {
        return id;
    }

    public StoreAbstract setId(long id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public StoreAbstract setName(String name) {
        this.name = name;
        return this;
    }

    public LocalDateTime getCreateAt() {
        return createAt;
    }

    public StoreAbstract setCreateAt(LocalDateTime createAt) {
        this.createAt = createAt;
        return this;
    }

    public LocalDateTime getUploadAt() {
        return uploadAt;
    }

    public StoreAbstract setUploadAt(LocalDateTime uploadAt) {
        this.uploadAt = uploadAt;
        return this;
    }

    public Trash getTrash() {
        return trash;
    }

    public StoreAbstract setTrash(Trash trash) {
        this.trash = trash;
        return this;
    }

    public LocalPath getPath() {
        return path;
    }

    public StoreAbstract setPath(LocalPath path) {
        this.path = path;
        return this;
    }

    public RemoveType getRemoveType() {
        return removeType;
    }

    public StoreAbstract setRemoveType(RemoveType removeType) {
        this.removeType = removeType;
        return this;
    }

    public Drive getDrive() {
        return drive;
    }

    public StoreAbstract setDrive(Drive drive) {
        this.drive = drive;
        return this;
    }

    public UUID getLink() {
        return link;
    }
    
    public StoreAbstract setLink(UUID link) {
        this.link = link;
        return this;
    }

    public boolean isHasLinkAccess() {
        return hasLinkAccess;
    }

    public StoreAbstract setHasLinkAccess(boolean hasLinkAccess) {
        this.hasLinkAccess = hasLinkAccess;
        return this;
    }

    public User getOwner() {
        return owner;
    }

    public StoreAbstract setOwner(User owner) {
        this.owner = owner;
        return this;
    }

    public PreRemove getRemove() {
        return preRemove;
    }

    public StoreAbstract setRemove(PreRemove remove) {
        this.preRemove = remove;
        return this;
    }
}
