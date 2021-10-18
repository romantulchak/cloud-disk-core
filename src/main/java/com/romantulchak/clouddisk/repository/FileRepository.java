package com.romantulchak.clouddisk.repository;

import com.romantulchak.clouddisk.model.File;
import com.romantulchak.clouddisk.model.enums.RemoveType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface FileRepository extends JpaRepository<File, Long> {

    List<File> findAllByFolderLinkAndRemoveType(UUID link, RemoveType removeType);

    List<File> findAllByTrashId(long id);

    List<File> findAllByDriveNameAndRemoveType(String driveName, RemoveType removeType);

    List<File> findAllByFolderLink(UUID link);

    Optional<File> findFileByLink(UUID link);

    boolean existsByOwnerIdAndLink(long id, UUID link);

    boolean existsByName(String name);

    @Modifying
    @Query(value = "DELETE FROM File f WHERE f.id = :id")
    void deleteById(@Param("id") long id);
}
