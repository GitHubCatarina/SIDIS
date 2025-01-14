package com.example.serviceLendingCom.lendingManagementCom.dto;

import lombok.Getter;
import lombok.Setter;

public class ReturnLendingTempRequest {
    @Getter
    @Setter
    private Long id;
    @Getter @Setter
    private String comment;
    @Getter @Setter
    private boolean recom;
    @Getter @Setter
    private String com;

    // Getters e setters
}
