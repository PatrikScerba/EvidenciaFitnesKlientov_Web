package sk.patrikscerba.gym.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import sk.patrikscerba.gym.dto.LoginRequest;
import sk.patrikscerba.gym.dto.LoginResponse;
import sk.patrikscerba.gym.entity.UserEntity;
import sk.patrikscerba.gym.exception.BusinessException;
import sk.patrikscerba.gym.repository.UserRepository;

/**
 * Implementácia servisu pre autentifikáciu používateľa.
 * Obsahuje logiku overenia prihlasovacích údajov.
 */
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    // Overí prihlasovacie údaje používateľa a vráti odpoveď s jeho základnými údajmi
    @Override
    public LoginResponse login(LoginRequest request){

        // Vyhľadá používateľa podľa emailu, ak neexistuje, vyhodí výnimku
        UserEntity user = userRepository.findByEmail(request.getEmail())
        .orElseThrow(()-> new BusinessException("Nesprávny email alebo heslo."));

        // Overí, či zadané heslo sedí s uloženým hashom v databáze
        if (!passwordEncoder.matches(request.getPassword(),user.getPassword())){
            throw new BusinessException("Nesprávny email alebo heslo.");
        }

        // Vytvorí odpoveď s údajmi o prihlásenom používateľovi
        LoginResponse response = new LoginResponse();
        response.setUserId(user.getUserId());
        response.setEmail(user.getEmail());
        response.setRole(user.getRole().name());
        response.setClientId(user.getClient() != null ? user.getClient().getClientId() : null);
        response.setMessage("Prihlásenie bolo úspešné.");

        return response;
    }
}
