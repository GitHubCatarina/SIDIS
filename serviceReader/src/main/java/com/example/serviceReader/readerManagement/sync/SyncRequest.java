package com.example.serviceReader.readerManagement.sync;

public class SyncRequest {
    private Long readerId;
    private String action;

    public SyncRequest() {
    }

    public SyncRequest(Long readerId, String action) {
        this.readerId = readerId;
        this.action = action;
    }

    public Long getReaderId() {
        return readerId;
    }

    public void setReaderId(Long readerId) {
        this.readerId = readerId;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }
}
