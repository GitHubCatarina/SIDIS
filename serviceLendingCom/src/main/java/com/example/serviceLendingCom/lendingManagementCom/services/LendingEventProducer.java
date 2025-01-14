package com.example.serviceLendingCom.lendingManagementCom.services;

import com.example.serviceLendingCom.lendingManagementCom.dto.LendingDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LendingEventProducer {

    private final RabbitTemplate rabbitTemplate;

    public void sendLendingCreatedEvent(LendingDTO lendingDTO) {
        rabbitTemplate.convertAndSend("lending.exchange", "", lendingDTO);
        System.out.println("Mensagem enviada: empréstimo criado com código " + lendingDTO.getLendingCode());
    }

    public void sendLendingReturnedEvent(LendingDTO lendingDTO) {
        rabbitTemplate.convertAndSend("lending.exchange", "", lendingDTO);
        System.out.println("Mensagem enviada: empréstimo devolvido com código " + lendingDTO.getLendingCode());
    }
}
