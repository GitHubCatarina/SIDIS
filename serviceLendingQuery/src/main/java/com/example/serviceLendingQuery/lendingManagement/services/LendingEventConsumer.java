package com.example.serviceLendingQuery.lendingManagement.services;

import com.example.serviceLendingQuery.lendingManagement.dto.LendingDTO;
import com.example.serviceLendingQuery.lendingManagement.model.Lending;
import com.example.serviceLendingQuery.lendingManagement.repositories.LendingRepository;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
public class LendingEventConsumer {

    private final LendingServiceImpl lendingService;
    private final LendingRepository lendingRepository;

    public LendingEventConsumer(LendingServiceImpl lendingService, LendingRepository lendingRepository) {
        this.lendingService = lendingService;
        this.lendingRepository = lendingRepository;
    }

    // Consumidor para a fila principal de sincronização
    @RabbitListener(queues = "#{lendingQueue.name}", ackMode = "AUTO")
    public void handleLendingCreatedEvent(LendingDTO lendingDTO) {
        System.out.println("Mensagem recebida para empréstimo com código: " + lendingDTO.getLendingCode());

        // Verificar se o empréstimo já existe pelo lendingCode
        boolean lendingExists = lendingService.lendingExists(lendingDTO.getLendingCode());

        if (lendingExists) {
            System.out.println("Empréstimo com código " + lendingDTO.getLendingCode() + " já existe, não será criado.");
        } else {
            // Criar um objeto Lending a partir do LendingDTO
            Lending lending = new Lending();
            lending.setLendingCode(lendingDTO.getLendingCode());
            lending.setReaderId(lendingDTO.getReaderId());
            lending.setBookId(lendingDTO.getBookId());
            lending.setBookTitle(lendingDTO.getBookTitle());
            lending.setLendDate(lendingDTO.getLendDate());
            lending.setLimitDate(lendingDTO.getLimitDate());
            lending.setReturnedDate(lendingDTO.getReturnedDate());
            lending.setReturned(lendingDTO.isReturned());
            lending.setFine(lendingDTO.getFine());
            lending.setComment(lendingDTO.getComment());

            // Usar o método de criação de empréstimo
            lendingRepository.save(lending); // Cria o empréstimo no sistema
            System.out.println("Empréstimo com código " + lendingDTO.getLendingCode() + " criado com sucesso.");
        }
    }
}