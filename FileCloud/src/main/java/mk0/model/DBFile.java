/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mk0.model;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 *
 * @author grizzardfamily
 */
@Entity
@Table(name="files")
public class DBFile {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;    
    private String fileName;    
    private String fileType;
    private String directory;   
    
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User owner;
    
    public DBFile() {        
    }   
    
    public DBFile(String sFileName, String sFileType, String sDirectory, User uOwner) {
        this.fileName = sFileName;
        this.fileType = sFileType;
        this.directory = sDirectory;
        this.owner = uOwner;
    }
    
    public Long getId() {
        return this.id;
    }
    
    public String getFileName() {
        return this.fileName;
    }
    
    public String getFileType() {
        return this.fileType;
    }    
    
    public String getDirectory() {
        return this.directory;
    }
    
    public User getOwner() {
        return this.owner;
    }
    
    public void setId(Long lId) {
        this.id = lId;
    }
    
    public void setFileName(String sFileName) {
        this.fileName = sFileName;
    }
    
    public void setFileType(String sFileType) {
        this.fileType = sFileType;
    }    
    
    public void setDirectory(String sDirectory) {
        this.directory = sDirectory;
    }
    
    public void setOwner(User uOwner) {
        this.owner = uOwner;
    }
}
