package com.example.serviceBookQuery.bookManagement.services;

public class BookEventMessage {
    private String bookJson;  // JSON do objeto CreateBookRequest
    private byte[] coverPhoto;  // A imagem como array de bytes

    // Construtores, getters e setters

    public BookEventMessage(String bookJson, byte[] coverPhoto) {
        this.bookJson = bookJson;
        this.coverPhoto = coverPhoto;
    }

    public String getBookJson() {
        return bookJson;
    }

    public void setBookJson(String bookJson) {
        this.bookJson = bookJson;
    }

    public byte[] getCoverPhoto() {
        return coverPhoto;
    }

    public void setCoverPhoto(byte[] coverPhoto) {
        this.coverPhoto = coverPhoto;
    }
}
