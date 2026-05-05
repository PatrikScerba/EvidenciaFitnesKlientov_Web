package sk.patrikscerba.gym.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sk.patrikscerba.gym.entity.ClientEntity;

import java.util.List;
import java.util.Optional;

/**
 * Repository vrstva pre prácu s entitou klienta.
 * Poskytuje základné CRUD operácie nad entitou ClientEntity.
 */
public interface ClientRepository extends JpaRepository<ClientEntity, Long> {

    // Vyhľadá klientov podľa mena bez ohľadu na veľkosť písmen.
    List<ClientEntity> findByFirstNameContainingIgnoreCase(String firstName);

    // Vyhľadá klientov podľa priezviska bez ohľadu na veľkosť písmen.
    List<ClientEntity> findByLastNameContainingIgnoreCase(String lastName);

    // Vyhľadá klientov podľa mena aj priezviska bez ohľadu na veľkosť písmen.
    List<ClientEntity> findByFirstNameContainingIgnoreCaseAndLastNameContainingIgnoreCase(String firstName, String lastName);

    // Vyhľadá klienta podľa emailu.
    Optional<ClientEntity> findByEmail(String email);

    // Skontroluje, či už v databáze existuje klient s daným emailom.
    boolean existsByEmail(String email);

    // Vyhľadá klienta podľa unikátneho QR tokenu.
    Optional<ClientEntity> findByQrToken(String qrToken);
}
