package com.romantulchak.clouddisk.repository;

import com.romantulchak.clouddisk.model.Trash;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TrashRepository extends JpaRepository<Trash, Long> {

    Optional<Trash> findTrashByDriveName(String driveName);

}
