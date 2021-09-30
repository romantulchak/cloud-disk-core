package com.romantulchak.clouddisk.repository;

import com.romantulchak.clouddisk.model.File;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FileRepository extends JpaRepository<File, Long> {
}
