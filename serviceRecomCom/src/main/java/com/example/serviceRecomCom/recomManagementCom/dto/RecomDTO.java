package com.example.serviceRecomCom.recomManagementCom.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RecomDTO {

    private Long id; // ID da recomendação

    @NotNull
    private String lendingId; // ID do empréstimo associado

    private Boolean recommend; // true = Recomendo, false = Não Recomendo

    private String comment; // Comentário opcional até 100 caracteres
}
