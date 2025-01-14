package com.example.serviceRecomCom.recomManagementCom.services;

import com.example.serviceRecomCom.recomManagementCom.dto.RecomDTO;
import com.example.serviceRecomCom.recomManagementCom.dto.ResponseDTO;
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
        // Enviar a mensagem para a fila do lending
        ResponseDTO responseDTO = new ResponseDTO();
        responseDTO.setLendingId(recomDTO.getLendingId());
        responseDTO.setRecommend(true);

        // Enviar a mensagem para o Lending
        rabbitTemplate.convertAndSend("recomlending.exchange", "", responseDTO);
        System.out.println("Mensagem enviada para lending com código: " + recomDTO.getLendingId());

    }

}
