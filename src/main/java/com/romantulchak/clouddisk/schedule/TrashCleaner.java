package com.romantulchak.clouddisk.schedule;

import com.romantulchak.clouddisk.model.File;
import com.romantulchak.clouddisk.model.PreRemove;
import com.romantulchak.clouddisk.model.StoreAbstract;
import com.romantulchak.clouddisk.model.Trash;
import com.romantulchak.clouddisk.repository.FileRepository;
import com.romantulchak.clouddisk.repository.FolderRepository;
import com.romantulchak.clouddisk.repository.PreRemoveRepository;
import com.romantulchak.clouddisk.repository.TrashRepository;
import com.romantulchak.clouddisk.utils.FileUtils;
import com.romantulchak.clouddisk.utils.FolderUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Component
public class TrashCleaner {

    private PreRemoveRepository removeRepository;
    private FileRepository fileRepository;
    private FolderRepository folderRepository;
    private FolderUtils folderUtils;

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

    @Scheduled(cron = "0 */10 * * * *")
    public void trashClean() {
        List<PreRemove> all = removeRepository.findAllByRemoveDate(LocalDate.now());
        for (PreRemove preRemove : all) {
            StoreAbstract element = preRemove.getElement();
            if (element instanceof File){
                removeFile((File)element, preRemove);
            }else{
                removeFolder();
            }
        }
    }


    private void removeFolder(){

    }

    private void removeFile(File file, PreRemove preRemove){
        folderUtils.removeElement(preRemove.getPath());
        removeRepository.deleteById(preRemove.getId());
        fileRepository.deleteById(file.getId());
    }

}
