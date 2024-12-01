package com.example.serviceAuth.userManagement.services;

import com.example.serviceAuth.userManagement.dto.UserDTO;
import com.example.serviceAuth.userManagement.model.User;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
public class AuthEventConsumer {

    private final UserService userService;

    public AuthEventConsumer(UserService userService) {
        this.userService = userService;
    }

    // Consumidor para a fila principal de sincronização
    @RabbitListener(queues = "#{authQueue.name}", ackMode = "AUTO")
    public void handleUserCreatedEvent(UserDTO userDTO) {
        System.out.println("Mensagem recebida para utilizador com ID: " + userDTO.getId());

        boolean usernameExists = userService.usernameExists(userDTO.getUsername());
        if (usernameExists) {
            System.out.println("Usuário já existe, não será criado.");
        } else {
            CreateUserRequest request = new CreateUserRequest(
                    userDTO.getUsername(),
                    userDTO.getFullName(),
                    userDTO.getPassword()
            );
            userService.create(request);  // Cria o utilizador
        }
    }
}
