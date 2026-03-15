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
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

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
                .cors(Customizer.withDefaults())

                // Vypnuté CSRF pre REST API
                .csrf(csrf -> csrf.disable())

                // Definuje pravidlá prístupu k endpointom
                .authorizeHttpRequests(auth -> auth

                        .requestMatchers("/").permitAll()

                        // Verejný endpoint pre prihlásenie.
                        .requestMatchers("/api/auth/login").permitAll()

                        // Verejný endpoint pre registráciu používateľa.
                        .requestMatchers("/api/account/register").permitAll()

                        // Endpointy sprístupnené podľa role používateľa.
                        .requestMatchers("/api/admin/**").hasRole("ADMIN")
                        .requestMatchers("/api/employee/**").hasAnyRole("ADMIN", "EMPLOYEE")
                        .requestMatchers("/api/client/**").hasRole("CLIENT")
                        .requestMatchers("/api/clients/**").hasAnyRole("ADMIN", "EMPLOYEE")

                        // Ostatné endpointy vyžadujú prihlásenie.
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

    // CORS konfigurácia umožňujúca komunikáciu React frontend-u so Spring Boot backendom.
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("http://localhost:3000", "http://localhost:5173"));
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(List.of("*"));
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
