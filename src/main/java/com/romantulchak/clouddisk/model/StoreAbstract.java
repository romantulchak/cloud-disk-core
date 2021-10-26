package com.romantulchak.clouddisk.model;

import com.romantulchak.clouddisk.dto.FolderDTO;
import com.romantulchak.clouddisk.model.enums.RemoveType;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@DiscriminatorColumn(name = "store_type")
public abstract class StoreAbstract implements Comparable<StoreAbstract> {

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

    @OneToOne(mappedBy = "element", orphanRemoval = true)
    private PreRemove preRemove;

    @OneToMany(mappedBy = "element", fetch = FetchType.EAGER)
    private List<Starred> starreds;

    @OneToOne(mappedBy = "element", orphanRemoval = true)
    private ElementAccess access;

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

    public PreRemove getPreRemove() {
        return preRemove;
    }

    public StoreAbstract setPreRemove(PreRemove preRemove) {
        this.preRemove = preRemove;
        return this;
    }

    public List<Starred> getStarreds() {
        return starreds;
    }

    public StoreAbstract setStarreds(List<Starred> starreds) {
        this.starreds = starreds;
        return this;
    }

    public ElementAccess getAccess() {
        return access;
    }

    public StoreAbstract setAccess(ElementAccess access) {
        this.access = access;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof StoreAbstract)) return false;
        StoreAbstract that = (StoreAbstract) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(createAt, that.createAt) && Objects.equals(link, that.link);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, createAt, link);
    }

    @Override
    public int compareTo(StoreAbstract o) {
        return Comparator.comparing(StoreAbstract::getName)
                .thenComparing(StoreAbstract::getCreateAt)
                .compare(this, o);
    }
}
