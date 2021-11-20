package com.romantulchak.clouddisk.repository;

import com.romantulchak.clouddisk.model.Folder;
import com.romantulchak.clouddisk.model.enums.RemoveType;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface FolderRepository extends JpaRepository<Folder, Long> {

    List<Folder> findAllByDriveNameAndRemoveType(String driveName, RemoveType removeType);

    Optional<Folder> findFolderByLink(UUID link);

    List<Folder> findFoldersByRootFolderAndRemoveType(UUID link, RemoveType removeType);

    boolean existsByLinkAndOwnerId(UUID folderLink, long userId);

    @EntityGraph(value = "Folder.subFolderFiles")
    Folder findByLink(UUID link);

    @Transactional
    @Modifying
    void deleteFolderByLink(UUID folderLink);

    List<Folder> findAllByTrashId(long id);
}
