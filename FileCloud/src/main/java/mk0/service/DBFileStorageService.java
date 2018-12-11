/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mk0.service;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import mk0.dao.DBFileRepo;
import mk0.exception.FileStorageException;
import mk0.exception.MyFileNotFoundException;
import mk0.model.DBFile;
import mk0.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author grizzardfamily
 */
@Service
public class DBFileStorageService {
    
    @Autowired
    private DBFileRepo dbFileRepo;
    
    @Value("${upload.file.directory}")
    private String uploadDirectory;
    
    public DBFile storeFile(MultipartFile file, User uOwner) {
        // Normalize file name
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());
        
        String fileDirectory = uploadDirectory + "/" + uOwner.getUsername();        

        try {
            // Check if the file's name contains invalid characters
            if(fileName.contains("..")) {
                throw new FileStorageException("Sorry! Filename contains invalid path sequence " + fileName);
            }
            
            DBFile dbFile = new DBFile(fileName, file.getContentType(), fileDirectory, uOwner);
            
            Path path = Paths.get(fileDirectory);
            //create user directory if it doesn't exist
            if(!Files.exists(path)) {
                Files.createDirectory(path);
            }
            path = Paths.get(fileDirectory, fileName);
            Files.copy(file.getInputStream(), path);

            return dbFileRepo.save(dbFile);
        } catch (IOException ex) {
            throw new FileStorageException("Could not store file " + fileName + ". Please try again!", ex);
        }
    }
    
    public DBFile getFile(Long fileId) {
        return dbFileRepo.findById(fileId)
                .orElseThrow(() -> new MyFileNotFoundException("File not found with id " + fileId));
    }
    
    public List<DBFile> findByUserId(User uOwner) {
        return dbFileRepo.findByOwner(uOwner);        
    }
    
    public void removeById(long lId) {
        DBFile fileToRemove = dbFileRepo.getOne(lId);
        String toRemoveFileName = fileToRemove.getFileName();
        String toRemoveFileDirectory = fileToRemove.getDirectory();
        Path path = Paths.get(toRemoveFileDirectory, toRemoveFileName);
        dbFileRepo.removeById(lId);
        try {
            Files.deleteIfExists(path);
            Path pCheckingIfEmpty = Paths.get(toRemoveFileDirectory);
            if(Files.isDirectory(pCheckingIfEmpty) && Files.list(pCheckingIfEmpty).count() == 0) {
                Files.deleteIfExists(pCheckingIfEmpty);
            }
        } catch(IOException ex) {
            throw new FileStorageException("Could not store file " + toRemoveFileName + ". Please try again!", ex);
        }
        
    }    
    
    public DBFile findByFileName(String sFileName) {
        return dbFileRepo.findByFileName(sFileName);
    }
    
    public Resource loadFileAsResource(String fileName) {
        DBFile fileToReturn = dbFileRepo.findByFileName(fileName);
        String toReturnFileName = fileToReturn.getFileName();
        String toReturnFileDirectory = fileToReturn.getDirectory();
        Path path = Paths.get(toReturnFileDirectory, toReturnFileName);
        try {            
            Resource resource = new UrlResource(path.toUri());
            if(resource.exists()) {
                return resource;
            } else {
                throw new MyFileNotFoundException("File not found " + fileName);
            }
        } catch (MalformedURLException ex) {
            throw new MyFileNotFoundException("File not found " + fileName, ex);
        }
    }
}

