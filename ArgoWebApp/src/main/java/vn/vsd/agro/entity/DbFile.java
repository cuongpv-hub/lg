package vn.vsd.agro.entity;

import java.io.InputStream;
import java.io.Serializable;
import java.util.Date;

public class DbFile implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7554122210866861409L;

	private String originalFileName;
    private String uniqueFileName;
    private Date uploadDate;
    private DbFileMeta metaData;
    private InputStream inputStream;
    private String contentType;
    
    public DbFile() {
        super();
    }

    public DbFile(String originalFileName, String uniqueFileName, Date uploadDate, 
    		DbFileMeta metaData, InputStream inputStream, String contentType) {
    	
        super();
        this.originalFileName = originalFileName;
        this.uniqueFileName = uniqueFileName;
        this.uploadDate = uploadDate;
        this.metaData = metaData;
        this.inputStream = inputStream;
        this.setContentType(contentType);
    }

    public String getOriginalFileName() {
        return originalFileName;
    }

    public void setOriginalFileName(String originalFileName) {
        this.originalFileName = originalFileName;
    }

    public String getUniqueFileName() {
        return uniqueFileName;
    }

    public void setUniqueFileName(String uniqueFileName) {
        this.uniqueFileName = uniqueFileName;
    }

    public Date getUploadDate() {
        return uploadDate;
    }

    public void setUploadDate(Date uploadDate) {
        this.uploadDate = uploadDate;
    }

    public DbFileMeta getMetaData() {
        return metaData;
    }

    public void setMetaData(DbFileMeta metaData) {
        this.metaData = metaData;
    }

    public InputStream getInputStream() {
        return inputStream;
    }

    public void setInputStream(InputStream inputStream) {
        this.inputStream = inputStream;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }
}
