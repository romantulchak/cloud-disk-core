package com.romantulchak.clouddisk.repository;

import com.romantulchak.clouddisk.model.Folder;
import com.romantulchak.clouddisk.model.StoreAbstract;
import com.romantulchak.clouddisk.model.enums.RemoveType;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface StoreRepository extends JpaRepository<StoreAbstract, Long> {

    List<StoreAbstract> findTop5ByNameIsContainingIgnoreCaseAndOwnerId(String name, long id);

    @Query(value = "SELECT sa.id FROM StoreAbstract sa WHERE sa.link = :link")
    Optional<Long> getElementIdByLink(@Param("link") UUID link);

    List<StoreAbstract> findAllByStarredsUserId(long id);

    Optional<StoreAbstract> findByLink(UUID link);

    boolean existsByLinkAndOwnerId(UUID link, long id);

    List<StoreAbstract> findAllByTrashId(long id);

    @Modifying
    @Query("UPDATE StoreAbstract s SET s.hasLinkAccess = :hasLinkAccess WHERE s.id = :id")
    void updateLinkAccess(@Param("hasLinkAccess") boolean hasLinkAccess, @Param("id") long id);

    List<StoreAbstract> findAllByDriveNameAndRemoveType(String driveName, RemoveType removeType);

    List<StoreAbstract> findFoldersByRootFolderAndRemoveType(UUID link, RemoveType removeType);
}
