package com.example.serviceLendingCom.lendingManagementCom.dto;

public class BookCoverDTO {
    private String coverUrl;
    private String imageBase64;  // Agora é uma string com a imagem codificada em base64
    private String contentType;

    public BookCoverDTO() {
    }

    // Novo construtor com três parâmetros
    public BookCoverDTO(String coverUrl, String imageBase64, String contentType) {
        this.coverUrl = coverUrl;
        this.imageBase64 = imageBase64;
        this.contentType = contentType;
    }

    // Getters e Setters
    public String getCoverUrl() {
        return coverUrl;
    }

    public void setCoverUrl(String coverUrl) {
        this.coverUrl = coverUrl;
    }

    public String getImageBase64() {
        return imageBase64;
    }

    public void setImageBase64(String imageBase64) {
        this.imageBase64 = imageBase64;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    @Override
    public String toString() {
        return "BookCoverDTO{" +
                "coverUrl='" + coverUrl + '\'' +
                ", imageBase64='" + imageBase64 + '\'' +
                ", contentType='" + contentType + '\'' +
                '}';
    }
}
