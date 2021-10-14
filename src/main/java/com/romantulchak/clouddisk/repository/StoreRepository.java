package com.romantulchak.clouddisk.repository;

import com.romantulchak.clouddisk.model.StoreAbstract;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface StoreRepository extends JpaRepository<StoreAbstract, Long> {

    List<StoreAbstract> findTop5ByNameIsContainingIgnoreCaseAndOwnerId(String name, long id);

    List<StoreAbstract> findAllByOwnerIdAndNoticed(long id, boolean isNoticed);

    Optional<StoreAbstract> findByLink(UUID link);
}
