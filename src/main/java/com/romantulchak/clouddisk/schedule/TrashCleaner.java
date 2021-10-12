package com.romantulchak.clouddisk.schedule;

import com.romantulchak.clouddisk.model.File;
import com.romantulchak.clouddisk.model.Folder;
import com.romantulchak.clouddisk.model.PreRemove;
import com.romantulchak.clouddisk.model.StoreAbstract;
import com.romantulchak.clouddisk.repository.FileRepository;
import com.romantulchak.clouddisk.repository.FolderRepository;
import com.romantulchak.clouddisk.repository.PreRemoveRepository;
import com.romantulchak.clouddisk.utils.FolderUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Component
public class TrashCleaner {

    private final PreRemoveRepository removeRepository;
    private final FileRepository fileRepository;
    private final FolderRepository folderRepository;
    private final FolderUtils folderUtils;

    @Autowired
    public TrashCleaner(PreRemoveRepository removeRepository,
                        FolderRepository folderRepository,
                        FileRepository fileRepository,
                        FolderUtils folderUtils) {
        this.removeRepository = removeRepository;
        this.folderRepository = folderRepository;
        this.fileRepository = fileRepository;
        this.folderUtils = folderUtils;
    }

    @Transactional
    @Async
    @Scheduled(cron = "0 */10 * * * *")
    public void trashClean() {
        List<PreRemove> all = removeRepository.findAllByRemoveDate(LocalDate.now());
        for (PreRemove preRemove : all) {
            removeElement(preRemove.getElement());
        }
    }

    private void removeElement(StoreAbstract element){
        folderUtils.removeElement(element.getRemove().getPath());
        if (element instanceof File){
            fileRepository.deleteById(element.getId());
        }else {
            folderRepository.deleteById(element.getId());
        }
    }
}
