package de.tuchemnitz.tomkr.msar.api;

public class UploadFileResponse {
    private String id;
    private String fileUri;
    private String fileType;
    private long size;

    public UploadFileResponse(String id, String fileUri, String fileType, long size) {
        this.id = id;
        this.fileUri = fileUri;
        this.fileType = fileType;
        this.size = size;
    }

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getFileUri() {
		return fileUri;
	}

	public void setFileUri(String fileUri) {
		this.fileUri = fileUri;
	}

	public String getFileType() {
		return fileType;
	}

	public void setFileType(String fileType) {
		this.fileType = fileType;
	}

	public long getSize() {
		return size;
	}

	public void setSize(long size) {
		this.size = size;
	}
    
    
}