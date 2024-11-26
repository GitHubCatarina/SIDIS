package com.example.serviceReaderCom.readerManagement.model;

import jakarta.persistence.*;

import java.util.Arrays;

@Entity
public class ReaderPhoto {
    @Id
    @GeneratedValue
    private Long id;

    @OneToOne
    private Reader reader;

    @Lob
    private byte[] image;

    private String contentType;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Reader getReader() {
        return reader;
    }

    public void setReader(Reader reader) {
        this.reader = reader;
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
        return "ReaderPhoto{" +
                "id=" + id +
                ", reader=" + reader +
                ", image=" + Arrays.toString(image) +
                ", contentType='" + contentType + '\'' +
                '}';
    }
}
