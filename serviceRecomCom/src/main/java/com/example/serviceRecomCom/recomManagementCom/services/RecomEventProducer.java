package com.example.serviceRecomCom.recomManagementCom.services;

import com.example.serviceRecomCom.recomManagementCom.dto.RecomDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RecomEventProducer {

    private final RabbitTemplate rabbitTemplate;

    public void sendRecomCreatedEvent(RecomDTO recomDTO) {
        rabbitTemplate.convertAndSend("recom.exchange", "", recomDTO);
        System.out.println("Mensagem enviada: recomendação criada com ID " + recomDTO.getId());
    }

}
