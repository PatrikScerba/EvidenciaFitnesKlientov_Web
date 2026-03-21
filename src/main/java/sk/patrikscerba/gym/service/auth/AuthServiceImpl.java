package sk.patrikscerba.gym.service.auth;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.stereotype.Service;
import sk.patrikscerba.gym.dto.auth.LoginRequest;
import sk.patrikscerba.gym.dto.auth.LoginResponse;
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
    private final AuthenticationManager authenticationManager;

    /**
     * Overí prihlasovacie údaje používateľa, vytvorí Spring Security autentifikáciu
     * a uloží ju do session. Pri úspechu vráti základné údaje o prihlásenom používateľovi.
     */
    @Override
    public LoginResponse login(LoginRequest request, HttpServletRequest httpRequest) {

        // Vyhľadá používateľa podľa emailu, ak neexistuje, vyhodí výnimku
        UserEntity user = userRepository.findByEmail(request.getEmail())
        .orElseThrow(()-> new BusinessException("Nesprávny email alebo heslo."));

        // Overí, či zadané heslo sedí s uloženým hashom v databáze
        if (!passwordEncoder.matches(request.getPassword(),user.getPassword())){
            throw new BusinessException("Nesprávny email alebo heslo.");
        }

        // Vytvorí autentifikačný token z emailu a hesla a odovzdá ho Spring Security na overenie.
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );

        // Vytvorí nový security context a uloží doň úspešne overeného používateľa.
        SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
        securityContext.setAuthentication(authentication);
        SecurityContextHolder.setContext(securityContext);

        // Získa alebo vytvorí HTTP session a uloží do nej security context,
        // aby používateľ ostal prihlásený aj pri ďalších requestoch.
        HttpSession session = httpRequest.getSession(true);
        session.setAttribute(
                HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY,
                securityContext
        );

        // Vytvorí odpoveď s údajmi o prihlásenom používateľovi
        LoginResponse response = new LoginResponse();
        response.setUserId(user.getUserId());
        response.setEmail(user.getEmail());
        response.setRole(user.getRole().name());
        response.setClientId(user.getClient() != null ? user.getClient().getClientId() : null);
        response.setMessage("Prihlásenie bolo úspešné.");

        return response;
    }

    // Vráti údaje o aktuálne prihlásenom používateľovi podľa emailu.
    @Override
    public LoginResponse getCurrentUser(String email) {

        UserEntity user = userRepository.findByEmail(email)
                .orElseThrow(() -> new BusinessException("Používateľ neexistuje."));

        // Vytvorí odpoveď s údajmi o aktuálne prihlásenom používateľovi.
        LoginResponse response = new LoginResponse();
        response.setUserId(user.getUserId());
        response.setEmail(user.getEmail());
        response.setRole(user.getRole().name());
        response.setClientId(user.getClient() != null ? user.getClient().getClientId() : null);

        return response;
    }
}
