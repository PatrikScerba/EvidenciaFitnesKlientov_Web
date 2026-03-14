package sk.patrikscerba.gym.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Konfiguračná trieda pre nastavenie Spring Security.
 * Definuje pravidlá prístupu k endpointom, prihlasovanie a šifrovanie hesiel.
 */
@Configuration
@RequiredArgsConstructor
public class ApplicationSecurityConfiguration {

    private final UserDetailsService userDetailsService;

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
                .authenticationProvider(authenticationProvider())

                .httpBasic(Customizer.withDefaults())

                .formLogin(form -> form.disable());

        return http.build();
    }

    // Vytvorí provider, ktorý overuje používateľa cez UserDetailsService
    // a porovnáva heslá pomocou BCrypt encoderu.
    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    // Encoder na hashovanie hesiel.
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // Sprístupní AuthenticationManager, ktorý sa používa pri vlastnom login procese
    // na autentifikáciu používateľa cez Spring Security.
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }
}
