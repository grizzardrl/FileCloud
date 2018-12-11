/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mk0.model;

/**
 *
 * @author grizzardfamily
 */
public class UploadFileResponse {
    private String fileName;
    private String fileDownloadUri;
    private String fileType;
    private long size;

    public UploadFileResponse(String fileName, String fileDownloadUri, String fileType, long size) {
        this.fileName = fileName;
        this.fileDownloadUri = fileDownloadUri;
        this.fileType = fileType;
        this.size = size;
    }

    public String getFileName() {
        return fileName;
    }
    
    public String getFileDownloadUri() {
        return fileDownloadUri;
    }
    
    public String getFileType() {
        return fileType;
    }
    
    public long getSize() {
        return size;
    }
    
    public void setFileName(String sFileName) {
        this.fileName = sFileName;
    }
    
    public void setFileDownloadUri(String sFileDownloadUri) {
        this.fileDownloadUri = sFileDownloadUri;
    }
    
    public void setFileType(String sFileType) {
        this.fileType = sFileType;
    }
    
    public void setSize(Long lSize) {
        this.size = lSize;
    }
}