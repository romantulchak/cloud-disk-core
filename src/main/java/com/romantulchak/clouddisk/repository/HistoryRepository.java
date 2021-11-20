package com.romantulchak.clouddisk.repository;

import com.romantulchak.clouddisk.model.history.History;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HistoryRepository extends JpaRepository<History, Long> {

    List<History> findAllByElementIdOrderByDateDesc(long id);
}
