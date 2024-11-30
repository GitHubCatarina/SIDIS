package com.example.serviceAuth.userManagement.services;

import com.example.serviceAuth.userManagement.dto.UserDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthEventProducer {

    private final RabbitTemplate rabbitTemplate;

    public void sendUserCreatedEvent(UserDTO userDTO) {
        rabbitTemplate.convertAndSend("auth.exchange", "auth.user.created", userDTO);
        System.out.println("Mensagem enviada: utilizador criado com ID " + userDTO.getId());
    }

}
