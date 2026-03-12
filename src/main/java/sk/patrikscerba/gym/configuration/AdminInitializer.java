package sk.patrikscerba.gym.configuration;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;
import sk.patrikscerba.gym.entity.UserEntity;
import sk.patrikscerba.gym.enums.Role;
import sk.patrikscerba.gym.repository.UserRepository;

/**
 * Inicializačná konfiguračná trieda, ktorá pri štarte aplikácie
 * automaticky vytvorí predvoleného admin používateľa, ak ešte neexistuje.
 */
@Configuration
@RequiredArgsConstructor
public class AdminInitializer {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    // Automatické vytvorenie a uloženie admin používateľa pri štarte aplikácie.
    @Bean
    public CommandLineRunner createAdmin() {
        return args -> {
            if (!userRepository.existsByEmail("admin@gym.local")) {
                UserEntity admin = new UserEntity();
                admin.setEmail("admin@gym.local");
                admin.setPassword(passwordEncoder.encode("Admin123!"));
                admin.setRole(Role.ADMIN);

                userRepository.save(admin);
            }
        };
    }
}
