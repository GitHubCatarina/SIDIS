package com.example.serviceReader.readerManagement.api;

public class LendingReaderView {
    private Long readerId;
    private Long lendingCount;

    public LendingReaderView(Long readerId, Long lendingCount) {
        this.readerId = readerId;
        this.lendingCount = lendingCount;
    }

    // Getters e Setters
    public Long getReaderId() {
        return readerId;
    }

    public void setReaderId(Long readerId) {
        this.readerId = readerId;
    }

    public Long getLendingCount() {
        return lendingCount;
    }

    public void setLendingCount(Long lendingCount) {
        this.lendingCount = lendingCount;
    }
}
