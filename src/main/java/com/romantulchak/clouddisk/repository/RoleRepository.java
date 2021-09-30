package com.romantulchak.clouddisk.repository;

import com.romantulchak.clouddisk.model.Role;
import com.romantulchak.clouddisk.model.enums.RoleType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {

    Optional<Role> findByName(RoleType name);
}
