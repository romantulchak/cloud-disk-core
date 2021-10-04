package com.romantulchak.clouddisk.repository;

import com.romantulchak.clouddisk.model.File;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface FileRepository extends JpaRepository<File, Long> {

    List<File> findAllByFolderLink(UUID link);

    List<File> findFirstByDriveName(String driveName);

}
