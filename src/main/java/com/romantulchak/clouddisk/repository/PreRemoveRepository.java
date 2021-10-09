package com.romantulchak.clouddisk.repository;

import com.romantulchak.clouddisk.model.PreRemove;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface PreRemoveRepository extends JpaRepository<PreRemove, Long> {

    List<PreRemove> findAllByRemoveDate(LocalDate removeDate);

    @Modifying
    void deleteByElementId(long elementId);
}
