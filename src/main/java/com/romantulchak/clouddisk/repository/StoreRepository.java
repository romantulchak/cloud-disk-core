package com.romantulchak.clouddisk.repository;

import com.romantulchak.clouddisk.model.StoreAbstract;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
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
}
