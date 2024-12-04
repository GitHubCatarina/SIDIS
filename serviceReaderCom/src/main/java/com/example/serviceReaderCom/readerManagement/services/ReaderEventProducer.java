package com.example.serviceReaderCom.readerManagement.services;

import com.example.serviceReaderCom.readerManagement.dto.ReaderDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReaderEventProducer {

    private final RabbitTemplate rabbitTemplate;

    public void sendReaderCreatedEvent(ReaderDTO readerDTO) {
        rabbitTemplate.convertAndSend("reader.exchange", "", readerDTO);
        System.out.println("Mensagem enviada: leitor criado com ID " + readerDTO.getId());
    }
}
