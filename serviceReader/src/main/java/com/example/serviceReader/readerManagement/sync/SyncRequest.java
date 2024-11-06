package com.example.serviceReader.readerManagement.sync;

import com.example.serviceReader.readerManagement.api.ReaderProfileView;
import com.example.serviceReader.readerManagement.services.EditReaderRequest;
import org.springframework.web.multipart.MultipartFile;

public class SyncRequest {
    private Long readerId;
    private String action;
    private EditReaderRequest resource; // Dados do perfil do leitor para sincronização
    private String photoBase64;
    private long desiredVersion;


    // Construtor padrão
    public SyncRequest() {}

    // Construtor com parâmetros
    public SyncRequest(Long readerId, String action, EditReaderRequest resource, String photoBase64, long desiredVersion) {
        this.readerId = readerId;
        this.action = action;
        this.resource = resource;
        this.photoBase64 = photoBase64;

        this.desiredVersion = desiredVersion;
    }
    // Getters e Setters
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

    public EditReaderRequest getResource() {
        return resource;
    }

    public void setResource(EditReaderRequest resource) {
        this.resource = resource;
    }
/*
    public MultipartFile getPhoto() {
        return photo;
    }

    public void setPhoto(MultipartFile photo) {
        this.photo = photo;
    }


 */
    public String getPhotoBase64() {
        return photoBase64;
    }

    public void setPhotoBase64(String photoBase64) {
        this.photoBase64 = photoBase64;
    }

    public long getDesiredVersion() {
        return desiredVersion;
    }

    public void setDesiredVersion(long desiredVersion) {
        this.desiredVersion = desiredVersion;
    }
}