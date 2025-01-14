package com.example.serviceLendingCom.lendingManagementCom.model;


import jakarta.persistence.Entity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
public class LendingTemp {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String lendingCode;
    private String comment;
    private boolean recom;
    private String com;

    @Override
    public String toString() {
        return "{" +
                "\"id\": " + id + "," +
                "\"comment\": \"" + comment + "\"," +
                "\"recommend\": " + recom + "," +
                "\"com\": \"" + com + "\"" +
                "}";
    }

}

