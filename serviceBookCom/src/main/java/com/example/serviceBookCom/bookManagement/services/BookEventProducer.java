package com.example.serviceBookCom.bookManagement.services;

import com.example.serviceBookCom.bookManagement.dto.SyncBookDTO;
import com.example.serviceBookCom.bookManagement.model.Book;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class BookEventProducer {

    private final RabbitTemplate rabbitTemplate;

    @Autowired
    private ObjectMapper objectMapper;  // Para serializar objetos Java em JSON

    public void sendBookCreatedEvent(CreateBookRequest resource, MultipartFile coverPhoto) throws Exception {
        // Serializa o recurso para JSON
        String bookJson = objectMapper.writeValueAsString(resource);

        // Se a imagem estiver presente, converta para byte array
        byte[] coverPhotoBytes = null;
        if (coverPhoto != null) {
            coverPhotoBytes = coverPhoto.getBytes();
        }

        // Cria uma mensagem com o objeto e o arquivo
        BookEventMessage bookEventMessage = new BookEventMessage(bookJson, coverPhotoBytes);

        // Envia a mensagem para o RabbitMQ
        rabbitTemplate.convertAndSend("book.exchange", "", bookEventMessage);
    }


}