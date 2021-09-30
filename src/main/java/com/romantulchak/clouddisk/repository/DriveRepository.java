package com.romantulchak.clouddisk.repository;

import com.romantulchak.clouddisk.model.Drive;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DriveRepository extends JpaRepository<Drive, Long> {

    Optional<Drive> findDriveByOwnerId(long id);

    Optional<Drive> findDriveByName(String name);

    boolean existsByOwnerIdAndName(long id, String name);

}
