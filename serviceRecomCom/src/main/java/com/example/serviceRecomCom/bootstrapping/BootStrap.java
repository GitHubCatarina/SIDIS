package com.example.serviceRecomCom.bootstrapping;

import com.example.serviceRecomCom.recomManagementCom.model.Recom;
import com.example.serviceRecomCom.recomManagementCom.repositories.RecomRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
@RequiredArgsConstructor
public class BootStrap {

    private final RecomRepository recomRepository;

    @PostConstruct
    public void init() {
        if (recomRepository.count() > 0) {
            System.out.println("A base de dados já contém dados. Bootstrap ignorado.");
            return;
        }

        System.out.println("A inicializar o bootstrap para Recom...");

        // Dados fixos para inserir no banco de dados
        List<Recom> recoms = Arrays.asList(
                new Recom("2024/1", true, "Comentário gerado automaticamente para Lending 2024/1"),
                new Recom("2024/2", false, "Comentário gerado automaticamente para Lending 2024/2"),
                new Recom("2024/3", true, null),
                new Recom("2024/4", true, "Comentário gerado automaticamente para Lending 2024/4"),
                new Recom("2024/5", false, "Comentário gerado automaticamente para Lending 2024/5"),
                new Recom("2024/6", true, "Comentário gerado automaticamente para Lending 2024/6"),
                new Recom("2024/7", false, "Comentário gerado automaticamente para Lending 2024/7"),
                new Recom("2024/8", true, null),
                new Recom("2024/9", true, "Comentário gerado automaticamente para Lending 2024/9"),
                new Recom("2024/10", false, "Comentário gerado automaticamente para Lending 2024/10"),
                new Recom("2024/11", true, "Comentário gerado automaticamente para Lending 2024/11"),
                new Recom("2024/12", false, "Comentário gerado automaticamente para Lending 2024/12"),
                new Recom("2024/13", true, null),
                new Recom("2024/14", true, "Comentário gerado automaticamente para Lending 2024/14"),
                new Recom("2024/15", false, "Comentário gerado automaticamente para Lending 2024/15"),
                new Recom("2024/16", true, "Comentário gerado automaticamente para Lending 2024/16"),
                new Recom("2024/17", false, "Comentário gerado automaticamente para Lending 2024/17"),
                new Recom("2024/18", true, null),
                new Recom("2024/19", true, "Comentário gerado automaticamente para Lending 2024/19"),
                new Recom("2024/20", false, "Comentário gerado automaticamente para Lending 2024/20"),
                new Recom("2024/21", true, "Comentário gerado automaticamente para Lending 2024/21"),
                new Recom("2024/22", false, "Comentário gerado automaticamente para Lending 2024/22"),
                new Recom("2024/23", true, null),
                new Recom("2024/24", true, "Comentário gerado automaticamente para Lending 2024/24"),
                new Recom("2024/25", false, "Comentário gerado automaticamente para Lending 2024/25"),
                new Recom("2024/26", true, "Comentário gerado automaticamente para Lending 2024/26")
        );

        // Salvar todos os registros fixos
        recomRepository.saveAll(recoms);

        System.out.println("Bootstrap concluído com sucesso.");
    }
}
