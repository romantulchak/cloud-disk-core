package com.romantulchak.clouddisk.repository;

import com.romantulchak.clouddisk.model.File;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface FileRepository extends JpaRepository<File, Long> {

    List<File> findAllByFolderLink(UUID link);

    List<File> findAllByDriveName(String driveName);

    Optional<File> findFileByLink(UUID link);

    boolean existsByOwnerIdAndLink(long id, UUID link);

    boolean existsByName(String name);
}
