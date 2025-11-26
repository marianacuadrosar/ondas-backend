package com.hidroterapia_ondas.config;

import com.hidroterapia_ondas.model.User;
import com.hidroterapia_ondas.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class DataInitializer {

    @Value("${app.admin.username}")
    private String adminUsername;
    @Value("${app.admin.password}")
    private String adminPassword;
    @Value("${app.admin.role}")
    private String adminRole;

    @Bean
CommandLineRunner initUsers(UserRepository userRepo, PasswordEncoder encoder) {

    return args -> {

        userRepo.findByUsername(adminUsername).ifPresentOrElse(u -> {
            // Ya existe, no hacer nada
        }, () -> {
            User admin = new User();
            admin.setUsername(adminUsername);
            admin.setPassword(encoder.encode(adminPassword));

            String role = adminRole;
            if (role == null || role.isBlank()) {
                role = "ROLE_ADMIN";
            } else if (!role.startsWith("ROLE_")) {
                role = "ROLE_" + role;
            }
            admin.setRole(role);

            userRepo.save(admin);
            System.out.println("Usuario admin creado: " + adminUsername + " con rol " + role);
        });
    };
}

}