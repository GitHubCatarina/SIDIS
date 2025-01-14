package com.example.serviceLendingCom.lendingManagementCom.services;

import com.example.serviceLendingCom.lendingManagementCom.dto.LendingDTO;
import com.example.serviceLendingCom.lendingManagementCom.dto.RecomResponseDTO;
import com.example.serviceLendingCom.lendingManagementCom.model.Lending;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Service
public class LendingEventConsumer {

    private final LendingServiceImpl lendingService;
    private final RabbitTemplate rabbitTemplate;

    public LendingEventConsumer(LendingServiceImpl lendingService, RabbitTemplate rabbitTemplate) {
        this.lendingService = lendingService;
        this.rabbitTemplate = rabbitTemplate;
    }

    @RabbitListener(queues = "#{lendingQueue.name}", ackMode = "AUTO")
    public void handleLendingEvent(LendingDTO lendingDTO) {
        System.out.println("Mensagem recebida para empréstimo com código: " + lendingDTO.getLendingCode());

        if (lendingDTO.isReturned()) {
            // Passar o comentário, que pode ser nulo ou vazio
            lendingService.markAsReturned(lendingDTO.getLendingCode(), lendingDTO.getComment());
            System.out.println("Empréstimo com código " + lendingDTO.getLendingCode() + " marcado como devolvido.");
        } else {
            // Verificar se o empréstimo já existe
            boolean lendingExists = lendingService.lendingExists(lendingDTO.getLendingCode());

            if (lendingExists) {
                System.out.println("Empréstimo com código " + lendingDTO.getLendingCode() + " já existe.");
            } else {
                CreateLendingRequest lendingRequest = new CreateLendingRequest();
                lendingRequest.setReaderId(lendingDTO.getReaderId());
                lendingRequest.setBookId(lendingDTO.getBookId());

                lendingService.createLending(lendingRequest);
                System.out.println("Empréstimo com código " + lendingDTO.getLendingCode() + " criado.");
            }
        }
    }
    // Consumir eventos da fila "recom"
    @RabbitListener(queues = "#{recomlendingQueue.name}", ackMode = "AUTO")
    public void handleRecomEvent(RecomResponseDTO recomDTO) {
        System.out.println("Mensagem recebida para recomendação com ID: " + recomDTO.getLendingId());

    }
}