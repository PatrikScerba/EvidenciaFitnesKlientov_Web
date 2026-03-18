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
 * automaticky vytvorí predvoleného zamestnanca, ak ešte neexistuje.
 */
@Configuration
@RequiredArgsConstructor
public class EmployeeInitializer {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Bean
    public CommandLineRunner createEmployee() {

        // Automatické vytvorenie a uloženie zamestnanca pri štarte aplikácie.
        return args -> {
            if (!userRepository.existsByEmail("employee@gym.local")) {
                UserEntity employee = new UserEntity();
                employee.setEmail("employee@gym.local");
                employee.setPassword(passwordEncoder.encode("Employee123!"));
                employee.setRole(Role.EMPLOYEE);

                userRepository.save(employee);
            }
        };
    }
}
