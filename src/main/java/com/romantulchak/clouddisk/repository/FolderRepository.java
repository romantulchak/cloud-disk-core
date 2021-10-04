package com.romantulchak.clouddisk.repository;

import com.romantulchak.clouddisk.model.Folder;
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

    List<Folder> findAllByDriveName(@Param("driveName") String driveName);

    @EntityGraph(value = "Folder.subFolders")
    Optional<Folder> findFolderByLink(UUID link);

    @Query(value = "SELECT DISTINCT id, create_at, has_link_access, link, name, upload_at, drive_id, owner_id, full_path, short_path" +
            " FROM folder f LEFT JOIN folder_sub_folders fsf on f.id = fsf.folder_id " +
            "WHERE f.id IN (SELECT fsf1.sub_folders_id FROM folder_sub_folders fsf1 " +
            "LEFT JOIN folder f2 on fsf1.folder_id = f2.id WHERE f2.link = :link)", nativeQuery = true)
    List<Folder> findSubFolders(@Param("link") UUID folderLink);

    boolean existsByLinkAndOwnerId(UUID folderLink, long userId);

    @Transactional
    @Modifying
    void deleteFolderByLink(UUID folderLink);
}
