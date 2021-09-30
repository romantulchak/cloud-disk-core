package com.romantulchak.clouddisk.repository;

import com.romantulchak.clouddisk.model.Folder;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FolderRepository extends JpaRepository<Folder, Long> {

    List<Folder> findAllByDriveName(String driveName);
}
