package com.romantulchak.clouddisk.repository;

import com.romantulchak.clouddisk.model.history.History;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface HistoryRepository extends JpaRepository<History, Long> {

    Slice<History> findAllByElementIdOrderByDateDesc(long id, Pageable pageable);
}
