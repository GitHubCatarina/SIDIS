package com.example.serviceAuth.userManagement.repositories;

import com.example.serviceAuth.exceptions.NotFoundException;
import com.example.serviceAuth.userManagement.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        User user1 = new User();
        user1.setUsername("testUser");
        user1.setEnabled(true);
        userRepository.save(user1);

        User user2 = new User();
        user2.setUsername("disabledUser");
        user2.setEnabled(false);
        userRepository.save(user2);
    }

    @Test
    void save() {
        User newUser = new User();
        newUser.setUsername("newUser");
        newUser.setEnabled(true);
        User savedUser = userRepository.save(newUser);

        assertNotNull(savedUser.getId(), "O ID do usuário salvo não deve ser nulo");
        assertEquals("newUser", savedUser.getUsername(), "O nome de usuário deve corresponder ao esperado");
    }

    @Test
    void getByUsername() {
        User foundUser = userRepository.getByUsername("testUser");

        assertNotNull(foundUser, "O usuário não deve ser nulo");
        assertEquals("testUser", foundUser.getUsername(), "O nome de usuário deve corresponder");
    }

    @Test
    void findByUsername() {
        Optional<User> foundUser = userRepository.findByUsername("testUser");

        assertTrue(foundUser.isPresent(), "O usuário deve estar presente");
        assertEquals("testUser", foundUser.get().getUsername(), "O nome de usuário deve corresponder");
    }

    @Test
    void existsByUsername() {
        boolean exists = userRepository.existsByUsername("testUser");
        assertTrue(exists, "O usuário deve existir");

        boolean notExists = userRepository.existsByUsername("nonExistentUser");
        assertFalse(notExists, "O usuário não deve existir");
    }
}
