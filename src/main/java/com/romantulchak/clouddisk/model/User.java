package com.romantulchak.clouddisk.model;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.Size;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "users", uniqueConstraints = {
        @UniqueConstraint(columnNames = "username"),
        @UniqueConstraint(columnNames = "email")
})
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Size(max = 50)
    private String firstName;

    @Size(max = 75)
    private String lastName;

    @Size(max = 90)
    private String username;

    @Size(max = 90)
    @Email
    private String email;

    private String password;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "users_role", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles;

    @OneToOne(mappedBy = "owner", orphanRemoval = true)
    private Drive drive;

    @OneToMany(mappedBy = "user")
    private List<Starred> starreds;

    @OneToMany(mappedBy = "user")
    private List<History> histories;

    public User(){}

    public User(long id, String firstName, String lastName){
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public User(@Size(max = 50) String firstName, @Size(max = 75) String lastName, @Size(max = 90) String username, @Size(max = 90) @Email String email, String password) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.username = username;
        this.email = email;
        this.password = password;
    }

    public long getId() {
        return id;
    }

    public User setId(long id) {
        this.id = id;
        return this;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Drive getDrive() {
        return drive;
    }

    public void setDrive(Drive drive) {
        this.drive = drive;
    }

    public List<Starred> getStarreds() {
        return starreds;
    }

    public void setStarreds(List<Starred> starreds) {
        this.starreds = starreds;
    }

    public List<History> getStatistics() {
        return histories;
    }

    public void setStatistics(List<History> histories) {
        this.histories = histories;
    }
}
