package sk.patrikscerba.gym.service.qr;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sk.patrikscerba.gym.dto.qr.QrCodeResponse;
import sk.patrikscerba.gym.dto.qr.QrTokenResetRequest;
import sk.patrikscerba.gym.entity.ClientEntity;
import sk.patrikscerba.gym.entity.UserEntity;
import sk.patrikscerba.gym.exception.BusinessException;
import sk.patrikscerba.gym.exception.NotFoundException;
import sk.patrikscerba.gym.repository.ClientRepository;
import sk.patrikscerba.gym.repository.UserRepository;

/**
 * Implementácia služby pre administrátorský reset QR tokenu klienta.
 * Služba vyhľadá používateľa podľa emailu, overí jeho bezpečnostnú odpoveď
 * a po úspešnom overení vygeneruje klientovi nový QR token.
 */
@Service
public class QrTokenResetServiceImpl implements QrTokenResetService {

    private final ClientRepository clientRepository;
    private final UserRepository userRepository;
    private final QrService qrService;
    private final PasswordEncoder passwordEncoder;

    public QrTokenResetServiceImpl(
            ClientRepository clientRepository,
            UserRepository userRepository,
            QrService qrService,
            PasswordEncoder passwordEncoder) {
        this.clientRepository = clientRepository;
        this.userRepository = userRepository;
        this.qrService = qrService;
        this.passwordEncoder = passwordEncoder;
    }

    // Resetuje QR token klienta po úspešnom overení bezpečnostnej odpovede.
    @Override
    @Transactional
    public QrCodeResponse resetQrToken(QrTokenResetRequest request) {

        // Vyhľadanie používateľa podľa emailu.
        UserEntity user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new NotFoundException("Používateľ sa nenašiel."));

        // Z používateľa získame priradený klientský profil.
        ClientEntity client = user.getClient();

        if (client == null) {
            throw new NotFoundException("Klientský profil používateľa sa nenašiel.");
        }

        // Odpoveď upravíme do rovnakého formátu, v akom bola pôvodne ukladaná.
        String securityAnswer = request.getSecurityAnswer().trim().toLowerCase();

        // Overenie bezpečnostnej odpovede.
        boolean answerMatches = passwordEncoder.matches(
                securityAnswer,
                user.getSecurityAnswerHash()
        );

        if (!answerMatches) {
            throw new BusinessException("Bezpečnostná odpoveď nie je správna.");
        }

        // Vygenerovanie nového QR tokenu a jeho nastavenie klientovi.
        // Starý QR token sa týmto prepíše.
        client.setQrToken(qrService.generateQrToken());

        ClientEntity savedClient = clientRepository.save(client);

        // Vytvorenie odpovede pre frontend.
        QrCodeResponse response = new QrCodeResponse();
        response.setClientId(savedClient.getClientId());
        response.setFirstName(savedClient.getFirstName());
        response.setLastName(savedClient.getLastName());
        response.setQrToken(savedClient.getQrToken());

        return response;
    }
}

