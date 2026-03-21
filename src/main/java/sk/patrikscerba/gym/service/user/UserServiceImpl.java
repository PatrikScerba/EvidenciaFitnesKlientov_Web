package sk.patrikscerba.gym.service.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import sk.patrikscerba.gym.dto.auth.UserRegisterRequest;
import sk.patrikscerba.gym.entity.UserEntity;
import sk.patrikscerba.gym.enums.Role;
import sk.patrikscerba.gym.exception.BusinessException;
import sk.patrikscerba.gym.exception.ConflictException;
import sk.patrikscerba.gym.repository.UserRepository;

/**
 * Implementácia servisnej vrstvy pre používateľa.
 * Zabezpečuje registráciu nového používateľa a základné validačné kontroly.
 */
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // Vytvorenie nového používateľa
    @Override
    public void create(UserRegisterRequest user) {

        // Kontrola zhody hesiel
        if (!user.getPassword().equals(user.getConfirmPassword())) {
            throw new BusinessException("Heslá sa nezhodujú");
        }

        // Kontrola, či email už nie je zaregistrovaný
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new ConflictException("Email je už zaregistrovaný.");
        }

        UserEntity userEntity = new UserEntity();

        // Nastavenie údajov používateľa
        userEntity.setEmail(user.getEmail());
        userEntity.setPassword(passwordEncoder.encode(user.getPassword()));
        userEntity.setRole(Role.CLIENT);

        // Uloženie používateľa do databázy
        try {
            userRepository.save(userEntity);
        } catch (DataIntegrityViolationException e) {
            throw new ConflictException("Duplicitný email");
        }
    }
}


