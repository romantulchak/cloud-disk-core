package com.romantulchak.clouddisk.repository;

import com.romantulchak.clouddisk.model.StoreAbstract;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StoreRepository extends JpaRepository<StoreAbstract, Long> {

    List<StoreAbstract> findTop5ByNameIsContainingIgnoreCaseAndOwnerId(String name, long id);
}
