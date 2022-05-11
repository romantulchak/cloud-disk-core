package com.romantulchak.clouddisk.repository;

import com.romantulchak.clouddisk.model.Folder;
import com.romantulchak.clouddisk.model.enums.RemoveType;
import com.romantulchak.clouddisk.projection.FolderOwnerProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface FolderRepository extends JpaRepository<Folder, Long> {

    List<Folder> findAllByDriveNameAndRemoveType(String driveName, RemoveType removeType);

    Optional<Folder> findFolderByLink(UUID link);

    Optional<FolderOwnerProjection> findByLink(UUID link);

    List<Folder> findFoldersByRootFolderAndRemoveType(UUID link, RemoveType removeType);

    boolean existsByLinkAndOwnerId(UUID folderLink, long userId);

    @Transactional
    @Modifying
    void deleteFolderByLink(UUID folderLink);

    List<Folder> findAllByTrashId(long id);
}
