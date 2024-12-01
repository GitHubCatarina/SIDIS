package com.example.serviceBookCom.bookManagement.services;

import com.example.serviceBookCom.bookManagement.dto.SyncBookDTO;
import com.example.serviceBookCom.bookManagement.model.Book;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BookEventProducer {

    private final RabbitTemplate rabbitTemplate;

    public void sendBookCreatedEvent(SyncBookDTO syncBookDTO) {
        rabbitTemplate.convertAndSend("book.exchange", "", syncBookDTO);
        System.out.println("Mensagem enviada: Livro criado com ID " + syncBookDTO.getId());
    }

}