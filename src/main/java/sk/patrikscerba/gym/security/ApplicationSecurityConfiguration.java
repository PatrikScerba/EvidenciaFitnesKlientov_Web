package sk.patrikscerba.gym.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Konfiguračná trieda pre nastavenie Spring Security.
 * Definuje pravidlá prístupu k endpointom, prihlasovanie a šifrovanie hesiel.
 */
@Configuration
public class ApplicationSecurityConfiguration {

    // Nastavenie bezpečnostných pravidiel aplikácie
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http

                // Vypnuté CSRF pre REST API
                .csrf(csrf -> csrf.disable())

                // Definuje pravidlá prístupu k endpointom
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/login").permitAll()
                        .requestMatchers("/api/account/register").permitAll()
                        .requestMatchers("/api/auth/login").permitAll()
                        .requestMatchers("/clients/**").authenticated()

                        // Ostatné endpointy vyžadujú prihlásenie
                        .anyRequest().authenticated()
                )

                // Predvolený formulár na prihlásenie.
                .formLogin(form -> form
                        .defaultSuccessUrl("/", true)
                        .permitAll());

        return http.build();
    }

    // Encoder na hashovanie hesiel.
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
