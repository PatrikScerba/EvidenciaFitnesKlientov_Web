package sk.patrikscerba.gym.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import sk.patrikscerba.gym.entity.UserEntity;
import sk.patrikscerba.gym.repository.UserRepository;

/**
 * Servisná trieda pre načítanie používateľa podľa emailu
 * pre potreby Spring Security pri prihlasovaní.
 */
@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    // Načíta používateľa podľa emailu.
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        UserEntity user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Používateľ s emailom " + email + " neexistuje."));

        // Vytvorenie bezpečnostného používateľa pre Spring Security.
        return User.builder()
                .username(user.getEmail())
                .password(user.getPassword())
                .roles(user.getRole().name())
                .build();
    }
}