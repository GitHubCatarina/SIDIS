package com.example.serviceRecomCom.recomManagementCom.model;

import com.example.serviceRecomCom.recomManagementCom.dto.RecomDTO;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
@Entity
@Table
public class Recom {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private String lendingId; // Referência ao Lending no formato "2024/1", "2024/2", etc.

    private Boolean recommend; // true = Recomendo, false = Não Recomendo

    @Column(length = 100)
    private String comment; // Comentário opcional até 100 caracteres

    public Recom() {
    }

    public Recom(String lendingId, Boolean recommend, String comment) {
        this.lendingId = lendingId;
        this.recommend = recommend;
        this.comment = comment;
    }

    // Getters e Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLendingId() {
        return lendingId;
    }

    public void setLendingId(String lendingId) {
        this.lendingId = lendingId;
    }

    public Boolean getRecommend() {
        return recommend;
    }

    public void setRecommend(Boolean recommend) {
        this.recommend = recommend;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    @Override
    public String toString() {
        return "Recommendation{" +
                "id=" + id +
                ", lendingId='" + lendingId + '\'' +
                ", recommend=" + recommend +
                ", comment='" + comment + '\'' +
                '}';
    }

    public RecomDTO toDTO() {
        RecomDTO recomDTO = new RecomDTO();
        recomDTO.setId(this.id);
        recomDTO.setLendingId(this.lendingId);
        recomDTO.setRecommend(this.recommend);
        recomDTO.setComment(this.comment);
        return recomDTO;
    }
}
