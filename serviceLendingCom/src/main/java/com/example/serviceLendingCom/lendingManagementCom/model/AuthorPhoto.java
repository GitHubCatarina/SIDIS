package com.example.serviceLendingCom.lendingManagementCom.model;

import jakarta.persistence.*;

import java.util.Arrays;

@Entity
public class AuthorPhoto {
    @Id
    @GeneratedValue
    private Long id;

    @OneToOne
    private Author author;

    @Lob
    private byte[] image;

    private String contentType;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Author getAuthor() {
        return author;
    }

    public void setAuthor(Author author) {
        this.author = author;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    @Override
    public String toString() {
        return "AuthorPhoto{" +
                "id=" + id +
                ", author=" + author +
                ", image=" + Arrays.toString(image) +
                ", contentType='" + contentType + '\'' +
                '}';
    }
}