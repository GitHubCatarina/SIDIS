package com.example.serviceAuth.userManagement.services;

import com.example.serviceAuth.userManagement.dto.UserDTO;
import com.example.serviceAuth.userManagement.model.User;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
public class AuthEventConsumer {

    private final UserService userService;

    public AuthEventConsumer(UserService userService) {
        this.userService = userService;
    }

    @RabbitListener(queues = "auth.queue")
    public void handleUserCreatedEvent(UserDTO userDTO) {
        System.out.println("Recebida mensagem de sincronização para utilizador com ID: " + userDTO.getId());

        // Verificar se o utilizador já existe
        boolean usernameExists = userService.usernameExists(userDTO.getUsername());
        if (usernameExists) {
            System.out.println("Usuário já existe, não será criado.");
        } else {
            // Criar o usuário usando o método create
            CreateUserRequest request = new CreateUserRequest(userDTO.getUsername(), userDTO.getFullName(), userDTO.getPassword());
            userService.create(request);  // Chama o método create
        }
    }

}
