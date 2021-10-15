package com.romantulchak.clouddisk.repository;

import com.romantulchak.clouddisk.model.ElementAccess;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface ElementAccessRepository extends JpaRepository<ElementAccess, Long> {

    Optional<ElementAccess> findByElementLink(UUID link);

}
