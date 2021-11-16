package com.romantulchak.clouddisk.dto;

import com.fasterxml.jackson.annotation.JsonView;
import com.mapperDTO.annotation.DTO;
import com.mapperDTO.annotation.MapToDTO;
import com.romantulchak.clouddisk.model.View;

import java.util.Set;

@DTO
public class UserDTO {
    private long id;

    @JsonView({View.FolderView.class,  View.FolderFileView.class, View.HistoryView.class})
    @MapToDTO(mapClass = {View.FolderView.class, View.FolderFileView.class, View.HistoryView.class})
    private String firstName;

    @JsonView({View.FolderView.class, View.FolderFileView.class, View.HistoryView.class})
    @MapToDTO(mapClass = {View.FolderView.class, View.FolderFileView.class, View.HistoryView.class})
    private String lastName;

    private String username;

    private String email;

    private String password;

    private Set<RoleDTO> roles;

    private DriveDTO drive;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Set<RoleDTO> getRoles() {
        return roles;
    }

    public void setRoles(Set<RoleDTO> roles) {
        this.roles = roles;
    }

    public DriveDTO getDrive() {
        return drive;
    }

    public void setDrive(DriveDTO drive) {
        this.drive = drive;
    }
}
