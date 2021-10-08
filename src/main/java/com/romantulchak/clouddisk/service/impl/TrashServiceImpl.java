package com.romantulchak.clouddisk.service.impl;

import com.romantulchak.clouddisk.exception.TrashNotFoundException;
import com.romantulchak.clouddisk.model.Drive;
import com.romantulchak.clouddisk.model.LocalPath;
import com.romantulchak.clouddisk.model.Store;
import com.romantulchak.clouddisk.model.Trash;
import com.romantulchak.clouddisk.repository.TrashRepository;
import com.romantulchak.clouddisk.service.TrashService;
import com.romantulchak.clouddisk.utils.FolderUtils;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TrashServiceImpl implements TrashService {

    private final TrashRepository trashRepository;
    private final FolderUtils folderUtils;

    public TrashServiceImpl(TrashRepository trashRepository,
                            FolderUtils folderUtils) {
        this.trashRepository = trashRepository;
        this.folderUtils = folderUtils;
    }

    @Override
    public Trash create(Drive drive) {
        String name = "Trash-" + drive.getName();
        Trash trash = new Trash()
                .setName(name)
                .setDrive(drive);
        String path = folderUtils.createTrash(name, drive.getShortPath());
        trash.setPath(path);
        return trashRepository.save(trash);
    }

    @Override
    public Trash getTrashByDriveName(String driveName) {
        return trashRepository.findTrashByDriveName(driveName)
                .orElseThrow(() -> new TrashNotFoundException(driveName));
    }

    @Override
    public List<Store> getRemovedElements(String driveName) {
//        Trash trash = getTrashByDriveName(driveName);
//        return folderService.findRemovedElementsByTrashId(trash.getId());
        return null;
    }

}
