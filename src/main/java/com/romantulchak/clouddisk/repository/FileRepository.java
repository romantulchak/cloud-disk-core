package com.romantulchak.clouddisk.repository;

import com.romantulchak.clouddisk.model.File;
import com.romantulchak.clouddisk.model.enums.RemoveType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface FileRepository extends JpaRepository<File, Long> {

    List<File> findAllByFolderLinkAndRemoveType(UUID link, RemoveType removeType);

    List<File> findAllByTrashId(long id);

    List<File> findAllByDriveNameAndRemoveType(String driveName, RemoveType removeType);

    Optional<File> findFileByLink(UUID link);

    boolean existsByOwnerIdAndLink(long id, UUID link);

    boolean existsByName(String name);
}
