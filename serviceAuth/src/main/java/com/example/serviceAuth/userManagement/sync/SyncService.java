package com.example.serviceAuth.userManagement.sync;

import com.example.serviceAuth.userManagement.model.User;
import com.example.serviceAuth.userManagement.repositories.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class SyncService {

    private final UserRepository userRepository;

    public SyncService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Transactional
    public void syncData(SyncRequest syncRequest) {
        System.out.println("Sync Request: " + syncRequest);

        Long userId = syncRequest.getId();
        if (userId == null) {
            throw new IllegalArgumentException("UserID não pode ser nulo");
        }

        // Procurar o recurso pelo userId fornecido no SyncRequest
        User existingUser = userRepository.findById(userId).orElse(null);

        if (existingUser != null) {
            // Atualiza o recurso existente com os dados do SyncRequest
            updateUser(existingUser, syncRequest.getResource());
            System.out.println("Recurso atualizado com sucesso.");
        } else {
            // Cria novo recurso com base nos dados do SyncRequest
            createUser(syncRequest);
            System.out.println("Recurso criado com sucesso.");
        }
    }

    private void updateUser(User existingUser, User resource) {
        // Atualiza os campos do objeto User existente com base no User recebido, se não forem nulos
        if (resource.getUsername() != null) {
            existingUser.setUsername(resource.getUsername());
        }
        if (resource.getPassword() != null) {
            existingUser.setPassword(resource.getPassword());
        }
        if (resource.getFullName() != null) {
            existingUser.setFullName(resource.getFullName());
        }
        if (!resource.getAuthorities().isEmpty()) {
            existingUser.getAuthorities().clear();
            existingUser.getAuthorities().addAll(resource.getAuthorities());
        }
        existingUser.setEnabled(resource.isEnabled());

        userRepository.save(existingUser);
    }

    private void createUser(SyncRequest syncRequest) {
        // Cria um novo objeto User e preenche com dados do SyncRequest
        User resource = syncRequest.getResource();
        User newUser = new User();
        newUser.setUsername(resource.getUsername());
        newUser.setPassword(resource.getPassword());
        newUser.setFullName(resource.getFullName());
        newUser.setEnabled(resource.isEnabled());
        newUser.getAuthorities().addAll(resource.getAuthorities());

        userRepository.save(newUser);
    }
}