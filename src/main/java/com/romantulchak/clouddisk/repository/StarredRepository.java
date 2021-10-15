package com.romantulchak.clouddisk.repository;

import com.romantulchak.clouddisk.model.Starred;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StarredRepository extends JpaRepository<Starred, Long> {

    Optional<Starred> findByElementIdAndUserId(long elementId, long userId);

}
