package com.example.serviceLendingCom.lendingManagementCom.model;


import jakarta.persistence.Entity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
public class LendingTemp {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter @Setter
    private Long id;
    @Getter @Setter
    private String lendingCode;
    @Getter @Setter
    private String comment;
    @Getter @Setter
    private boolean recom;
    @Getter @Setter
    private String com;

    // Getters e setters
}
