package com.example.serviceAuth.bootstrapping;

import com.example.serviceAuth.userManagement.model.Role;
import com.example.serviceAuth.userManagement.model.User;
import com.example.serviceAuth.userManagement.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
@Profile("bootstrap")
public class UserBootstrapper implements CommandLineRunner {

    @Autowired
    private final UserRepository userRepo;

    private final PasswordEncoder encoder;

    @Override
    @Transactional
    public void run(final String... args) throws Exception {
        System.out.println("Bootstrap profile is active");

        if (userRepo.findByUsername("admin1@mail.com").isEmpty()) {
            final User admin1 = new User("admin1@mail.com", encoder.encode("admin"), "Admin");
            admin1.addAuthority(new Role(Role.ADMIN));
            userRepo.save(admin1);
        }

        if (userRepo.findByUsername("reader1@mail.com").isEmpty()) {
            final User reader1 = new User("reader1@mail.com", encoder.encode("password"),  "Reader");
            reader1.addAuthority(new Role(Role.READER));
            userRepo.save(reader1);
        }

        if (userRepo.findByUsername("librarian1@mail.com").isEmpty()) {
            final var librarian1 = new User("librarian1@mail.com", encoder.encode("password"), "Librarian");
            librarian1.addAuthority(new Role(Role.LIBRARIAN));
            userRepo.save(librarian1);
        }

        if (userRepo.findByUsername("librarian2@mail.com").isEmpty()) {
            final var librarian2 = new User("librarian2@mail.com", encoder.encode("password"), "Librarian");
            librarian2.addAuthority(new Role(Role.LIBRARIAN));
            userRepo.save(librarian2);
        }

        if (userRepo.findByUsername("librarian3@mail.com").isEmpty()) {
            final var librarian3 = new User("librarian3@mail.com", encoder.encode("password"), "Librarian");
            librarian3.addAuthority(new Role(Role.LIBRARIAN));
            userRepo.save(librarian3);
        }

        if (userRepo.findByUsername("librarian4@mail.com").isEmpty()) {
            final var librarian4 = new User("librarian4@mail.com", encoder.encode("password"), "Librarian");
            librarian4.addAuthority(new Role(Role.LIBRARIAN));
            userRepo.save(librarian4);
        }

        if (userRepo.findByUsername("librarian5@mail.com").isEmpty()) {
            final var librarian5 = new User("librarian5@mail.com", encoder.encode("password"), "Librarian");
            librarian5.addAuthority(new Role(Role.LIBRARIAN));
            userRepo.save(librarian5);
        }

        if (userRepo.findByUsername("librarian6@mail.com").isEmpty()) {
            final var librarian6 = new User("librarian6@mail.com", encoder.encode("password"), "Librarian");
            librarian6.addAuthority(new Role(Role.LIBRARIAN));
            userRepo.save(librarian6);
        }

        if (userRepo.findByUsername("librarian7@mail.com").isEmpty()) {
            final var librarian7 = new User("librarian7@mail.com", encoder.encode("password"), "Librarian");
            librarian7.addAuthority(new Role(Role.LIBRARIAN));
            userRepo.save(librarian7);
        }

        if (userRepo.findByUsername("librarian8@mail.com").isEmpty()) {
            final var librarian8 = new User("librarian8@mail.com", encoder.encode("password"), "Librarian");
            librarian8.addAuthority(new Role(Role.LIBRARIAN));
            userRepo.save(librarian8);
        }

        if (userRepo.findByUsername("librarian9@mail.com").isEmpty()) {
            final var librarian9 = new User("librarian9@mail.com", encoder.encode("password"), "Librarian");
            librarian9.addAuthority(new Role(Role.LIBRARIAN));
            userRepo.save(librarian9);
        }

        if (userRepo.findByUsername("librarian10@mail.com").isEmpty()) {
            final var librarian10 = new User("librarian10@mail.com", encoder.encode("password"), "Librarian");
            librarian10.addAuthority(new Role(Role.LIBRARIAN));
            userRepo.save(librarian10);
        }
    }
}