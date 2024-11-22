package com.example.serviceLendingQuery.lendingManagementQuery.api;

public class LentBookView {
    private Long bookId;
    private Long lendCount;

    // Construtor
    public LentBookView(Long bookId, Long lendCount) {
        this.bookId = bookId;
        this.lendCount = lendCount;
    }

    // Getters e Setters
    public Long getBookId() {
        return bookId;
    }

    public void setBookId(Long bookId) {
        this.bookId = bookId;
    }

    public Long getLendCount() {
        return lendCount;
    }

    public void setLendCount(Long lendCount) {
        this.lendCount = lendCount;
    }
}
