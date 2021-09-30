package com.romantulchak.clouddisk.repository;

import com.romantulchak.clouddisk.model.Drive;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DriveRepository extends JpaRepository<Drive, Long> {
}
