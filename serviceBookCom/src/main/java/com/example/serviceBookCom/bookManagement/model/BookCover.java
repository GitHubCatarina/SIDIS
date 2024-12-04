package com.example.serviceBookCom.bookManagement.model;

import jakarta.persistence.*;
import lombok.Getter;

import java.util.Arrays;

@Getter
@Entity
public class BookCover {

    @Id
    @GeneratedValue
    private Long id;
/*
    @OneToOne
    private Book book;
*/

    // This method is required to fetch the cover URL
    private String coverUrl;

    @Lob
    private byte[] image;

    private String contentType;

    public void setId(Long id) {
        this.id = id;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public void setCoverUrl(String coverUrl) {
        this.coverUrl = coverUrl;
    }

    @Override
    public String toString() {
        return "BookCover{" +
                "id=" + id +
//                ", book=" + book +
                ", image=" + Arrays.toString(image) +
                ", contentType='" + contentType + '\'' +
                '}';
    }

    public BookCover() {
    }

    public BookCover(String coverUrl, byte[] image) {
        this.coverUrl = coverUrl;
        this.image = image;
    }
}
