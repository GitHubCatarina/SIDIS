package com.example.serviceAuth.userManagement.sync;

import com.example.serviceAuth.userManagement.dto.UserDTO;
import com.example.serviceAuth.userManagement.model.Role;
import com.example.serviceAuth.userManagement.model.User;
import com.example.serviceAuth.userManagement.repositories.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.stream.Collectors;

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

        User existingUser = userRepository.findById(userId).orElse(null);

        if (existingUser != null) {
            updateUser(existingUser, syncRequest.getResource());
            System.out.println("Recurso atualizado com sucesso.");
        } else {
            createUser(syncRequest.getResource());
            System.out.println("Recurso criado com sucesso.");
        }
    }

    private void updateUser(User existingUser, UserDTO resourceDTO) {
        if (resourceDTO.getFullName() != null) {
            existingUser.setFullName(resourceDTO.getFullName());
        }
        if (resourceDTO.getPassword() != null) {
            existingUser.setPassword(resourceDTO.getPassword());
        }
        if (resourceDTO.getUsername() != null) {
            existingUser.setUsername(resourceDTO.getUsername());
        }
        if (!resourceDTO.getAuthorities().isEmpty()) {
            existingUser.getAuthorities().clear();
            existingUser.getAuthorities().addAll(
                    resourceDTO.getAuthorities().stream()
                            .map(Role::new)  // Converter cada String para um Role
                            .collect(Collectors.toSet())
            );
        }
        existingUser.setEnabled(resourceDTO.isEnabled());

        userRepository.save(existingUser);
    }

    private void createUser(UserDTO resourceDTO) {
        User newUser = new User();
        newUser.setUsername(resourceDTO.getUsername());
        newUser.setPassword(resourceDTO.getPassword());
        newUser.setFullName(resourceDTO.getFullName());
        newUser.setEnabled(resourceDTO.isEnabled());
        newUser.getAuthorities().addAll(
                resourceDTO.getAuthorities().stream()
                        .map(Role::new)  // Converter cada String para um Role
                        .collect(Collectors.toSet())
        );

        userRepository.save(newUser);
    }
}
