package com.example.serviceLendingCom.lendingManagementCom.services;

import com.example.serviceLendingCom.lendingManagementCom.dto.LendingDTO;
import com.example.serviceLendingCom.lendingManagementCom.model.Lending;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
public class LendingEventConsumer {

    private final LendingServiceImpl lendingService;

    public LendingEventConsumer(LendingServiceImpl lendingService) {
        this.lendingService = lendingService;
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
            // Criar um objeto CreateLendingRequest a partir do LendingDTO
            CreateLendingRequest lendingRequest = new CreateLendingRequest();
            lendingRequest.setReaderId(lendingDTO.getReaderId());
            lendingRequest.setBookId(lendingDTO.getBookId());

            // Usar o método de criação de empréstimo
            lendingService.createLending(lendingRequest);  // Cria o empréstimo no sistema
            System.out.println("Empréstimo com código " + lendingDTO.getLendingCode() + " criado com sucesso.");
        }
    }
}