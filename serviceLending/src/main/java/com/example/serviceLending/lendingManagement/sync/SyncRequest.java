package com.example.serviceLending.lendingManagement.sync;

public class SyncRequest {
    private Long lendingId;
    private String action;

    public SyncRequest(Long lendingId, String action) {
        this.lendingId = lendingId;
        this.action = action;
    }

    public Long getLendingId() {
        return lendingId;
    }

    public void setLendingId(Long lendingId) {
        this.lendingId = lendingId;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }
}
