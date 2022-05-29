package com.romantulchak.clouddisk.service.impl;

import com.mapperDTO.mapper.EntityMapperInvoker;
import com.romantulchak.clouddisk.constant.ApplicationConstant;
import com.romantulchak.clouddisk.dto.FileDTO;
import com.romantulchak.clouddisk.dto.FolderDTO;
import com.romantulchak.clouddisk.exception.DriveNotFoundException;
import com.romantulchak.clouddisk.exception.FolderNotFoundException;
import com.romantulchak.clouddisk.model.*;
import com.romantulchak.clouddisk.model.enums.ContextType;
import com.romantulchak.clouddisk.model.enums.HistoryType;
import com.romantulchak.clouddisk.model.enums.RemoveType;
import com.romantulchak.clouddisk.repository.DriveRepository;
import com.romantulchak.clouddisk.repository.ElementAccessRepository;
import com.romantulchak.clouddisk.repository.FolderRepository;
import com.romantulchak.clouddisk.service.FileService;
import com.romantulchak.clouddisk.service.FolderService;
import com.romantulchak.clouddisk.service.HistoryService;
import com.romantulchak.clouddisk.utils.FileUtils;
import com.romantulchak.clouddisk.utils.FolderUtils;
import com.romantulchak.clouddisk.utils.StoreUtils;
import com.romantulchak.clouddisk.utils.ZipUtils;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Service
public class FolderServiceImpl implements FolderService {

    private final FolderRepository folderRepository;
    private final DriveRepository driveRepository;
    private final FileService fileService;
    private final FolderUtils folderUtils;
    private final ElementAccessRepository elementAccessRepository;
    private final EntityMapperInvoker<Folder, FolderDTO> entityMapperInvoker;
    private final HistoryService historyService;

    public FolderServiceImpl(FolderRepository folderRepository,
                             DriveRepository driveRepository,
                             FileService fileService,
                             FolderUtils folderUtils,
                             HistoryService historyService,
                             ElementAccessRepository elementAccessRepository,
                             EntityMapperInvoker<Folder, FolderDTO> entityMapperInvoker) {
        this.folderRepository = folderRepository;
        this.driveRepository = driveRepository;
        this.fileService = fileService;
        this.folderUtils = folderUtils;
        this.historyService = historyService;
        this.entityMapperInvoker = entityMapperInvoker;
        this.elementAccessRepository = elementAccessRepository;
    }

    @Transactional
    @Override
    public FolderDTO create(String folderName, String driveName, Authentication authentication) {
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        User user = new User(userDetails.getId(), userDetails.getFirstName(), userDetails.getLastName());
        Drive drive = driveRepository.findDriveByName(driveName)
                .orElseThrow(() -> new DriveNotFoundException(user.getUsername()));
        Folder folder = getFolder(folderName, user, drive.getShortPath())
                .setDrive(drive);
        folder = folderRepository.save(folder);
        historyService.create(folder, HistoryType.CREATE);
        return convertToDTO(folder, View.FolderView.class);
    }

    @Override
    public Folder findFolderByLink(UUID folderLink) {
        return folderRepository.findFolderByLink(folderLink)
                .orElseThrow((() -> new FolderNotFoundException(folderLink)));
    }

    private Folder getFolder(String folderName, User user, String shortPath) {
        UUID link = UUID.randomUUID();
        Folder folder = new Folder()
                .setName(folderName)
                .setOwner(user)
                .setLink(link)
                .setCreateAt(LocalDateTime.now())
                .setUploadAt(LocalDateTime.now());
        LocalPath path = folderUtils.createFolderWithEncodedName(shortPath, folderName);
        folder.setPath(path);
        return folder;
    }

    private Folder getSubFolder(String folderName, Authentication authentication, String shortPath) {
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        User user = new User(userDetails.getId(), userDetails.getFirstName(), userDetails.getLastName());
        return getFolder(folderName, user, shortPath);

    }

    @Transactional
    @Override
    public FolderDTO createSubFolder(String folderName, UUID folderLink, Authentication authentication) {
        Folder mainFolder = folderRepository.findFolderByLink(folderLink)
                .orElseThrow(() -> new FolderNotFoundException(folderLink));
        Folder subFolder = getSubFolder(folderName, authentication, mainFolder.getPath().getShortPath())
                .setRootFolder(mainFolder.getLink());
        subFolder = folderRepository.save(subFolder);
        historyService.createUploadHistory(mainFolder, subFolder, HistoryType.CREATE, ContextType.FOLDER, authentication);
        if (mainFolder.getAccess() != null) {
            ElementAccess elementAccess = new ElementAccess()
                    .setElement(subFolder)
                    .setAccessType(mainFolder.getAccess().getAccessType().name());
            subFolder.setAccess(elementAccess);
            elementAccessRepository.save(elementAccess);
        }
        return convertToDTO(subFolder, View.FolderView.class);
    }

    @Override
    public List<Store> findSubFoldersInFolder(UUID folderLink, String page) {
        List<FolderDTO> subFolders = folderRepository.findFoldersByRootFolderAndRemoveType(folderLink, RemoveType.SAVED)
                .stream()
                .sorted()
                .map(folder -> convertToDTO(folder, View.FolderFileView.class))
                .collect(Collectors.toList());
        List<FileDTO> filesInFolder = fileService.findFilesInFolder(folderLink, page);
        List<Store> stores = new ArrayList<>();
        stores.addAll(subFolders);
        stores.addAll(filesInFolder);
        return stores;
    }

    @Override
    public ResponseEntity<Resource> downloadFolder(UUID folderLink) throws IOException {
        Folder folder = folderRepository.findFolderByLink(folderLink)
                .orElseThrow(() -> new FolderNotFoundException(folderLink));
        Path path = ZipUtils.createZip(folder.getName(), folder.getPath().getShortPath());
        return FileUtils.getResource(path);
    }

    @Override
    public FolderDTO changeColor(UUID folderLink, String color) {
        Folder folder = findFolderByLink(folderLink);
        if (!folder.getColor().equals(color)) {
            folder.setColor(color);
            folderRepository.save(folder);
        }
        return convertToDTO(folder, View.FolderFileView.class);
    }

    @Async
    @Transactional
    @Override
    public CompletableFuture<FolderDTO> uploadInDrive(List<MultipartFile> files, String driveName) {
        Drive drive = driveRepository.findDriveByName(driveName)
                .orElseThrow(() -> new DriveNotFoundException(driveName));
        List<Folder> folders = uploadFolder(files, drive.getOwner(), drive.getShortPath());
        Folder folder = folders.get(0);
        folder = folder.setDrive(drive);
        folderRepository.saveAll(folders);
        return CompletableFuture.completedFuture(convertToDTO(folder, View.FolderFileView.class));
    }

    @Async
    @Transactional
    @Override
    public CompletableFuture<FolderDTO> uploadInFolder(List<MultipartFile> files, String folderLink) {
        UUID link = UUID.fromString(folderLink);
        Folder folder = folderRepository.findFolderByLink(link).orElseThrow(() -> new FolderNotFoundException(link));
        List<Folder> folders = uploadFolder(files, folder.getOwner(), folder.getPath().getShortPath());
        Folder subFolder = folders.get(0);
        subFolder.setRootFolder(link);
        folders.add(folder);
        folderRepository.saveAll(folders);
        return CompletableFuture.completedFuture(convertToDTO(subFolder, View.FolderFileView.class));
    }

    private Folder checkIfMainFolderExists(String folderName, String pathToFile, List<Folder> folders, User user) {
        String[] folderPathParts = folderName.split(ApplicationConstant.SLASH);
        StringBuilder sb = new StringBuilder();
        Folder folder = null;
        for (String value : folderPathParts) {
            sb.append(value)
                    .append(ApplicationConstant.SLASH);
            String mainFolderIdentifier = sb.toString().replaceAll(ApplicationConstant.LAST_SLASH_REGEX, "");
            String mainFolderName = FileUtils.getMainFolderName(sb.toString());
            if (folders.stream().noneMatch(e -> e.getName().equals(value) && e.getMainFolderIdentifier().equals(mainFolderIdentifier))) {
                String parentFolderName = FileUtils.getParentFolderPath(sb.toString());
                Optional<Folder> mainFolder = folders.stream().filter(f -> f.getName().equals(parentFolderName) &&
                        f.getMainFolderIdentifier().equals(mainFolderName)).findFirst();
                if (mainFolder.isPresent()) {
                    pathToFile = mainFolder.get().getPath().getShortPath();
                }
                folder = getFolder(value, user, pathToFile)
                        .setMainFolderIdentifier(mainFolderIdentifier);
                if (mainFolder.isPresent()) {
                    folder.setRootFolder(mainFolder.get().getLink());
                }
                folders.add(folder);
            }
        }
        return folder;
    }


    private List<Folder> uploadFolder(List<MultipartFile> files, User owner, String path) {
        List<String> createdFolders = new ArrayList<>();
        List<Folder> folders = new ArrayList<>();
        Folder subFolder = null;
        for (MultipartFile file : files) {
            String mainFolderPath = FileUtils.getFolderPath(file.getOriginalFilename());
            if (!createdFolders.contains(mainFolderPath)) {
                subFolder = checkIfMainFolderExists(mainFolderPath, path, folders, owner);
                createdFolders.add(mainFolderPath);
            }
            fileService.getFile(file, FileUtils.getName(file.getOriginalFilename()), owner, subFolder);
        }
        return folders;
    }

    private FolderDTO convertToDTO(Folder folder, Class<?> classToCheck) {
        return entityMapperInvoker.entityToDTO(folder, FolderDTO.class, classToCheck)
                .setNoticed(StoreUtils.isStarred(folder))
                .setOwner(StoreUtils.isOwner(folder));
    }

}
