package com.example.serviceTopQuery.topManagement.services;

import com.example.serviceTopQuery.topManagement.model.Lending;
import com.example.serviceTopQuery.topManagement.repositories.LendingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
@Service
public class LendingService {

    @Value("${lending.lateFee}")
    private float lateFee;

    private final LendingRepository lendingRepository;

    @Autowired
    public LendingService(LendingRepository lendingRepository) {
        this.lendingRepository = lendingRepository;
    }

    private float calculateFine(long daysOverdue) {
        return daysOverdue * lateFee;
    }

    public boolean lendingExists(String lendingCode) {
        // Verifica se já existe um empréstimo com o lendingCode
        return lendingRepository.findByLendingCode(lendingCode).isPresent();
    }


    public void markAsReturned(String lendingCode, String comment) {
        // Buscar o empréstimo pelo código
        Lending lending = lendingRepository.findByLendingCode(lendingCode)
                .orElseThrow(() -> new IllegalArgumentException("Empréstimo não encontrado: " + lendingCode));

        // Atualizar os campos para refletir a devolução
        lending.setReturned(true);
        lending.setReturnedDate(LocalDate.now());
        lending.setDaysTillReturn(0);

        // Calcular dias de atraso e multa (se houver)
        int daysOverdue = (int) ChronoUnit.DAYS.between(lending.getLimitDate(), LocalDate.now());
        if (daysOverdue < 0) {
            daysOverdue = 0;
        }
        lending.setDaysOverdue(daysOverdue);
        lending.setFine(calculateFine(daysOverdue));

        // Se um comentário foi fornecido, usá-lo. Caso contrário, deixar o campo de comentário vazio.
        if (comment != null && !comment.isEmpty()) {
            lending.setComment(comment);
        }

        // Salvar as alterações no repositório
        lendingRepository.save(lending);
    }
}

