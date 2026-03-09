package sk.patrikscerba.gym.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sk.patrikscerba.gym.entity.ClientEntity;

import java.util.List;
import java.util.Optional;

/**
 * Repository vrstva pre prácu s entitou klienta.
 * Poskytuje základné CRUD operácie nad entitou ClientEntity.
 */
public interface ClientRepository extends JpaRepository<ClientEntity,Long> {

    List<ClientEntity>findByFirstNameContainingIgnoreCase(String firstName);
    List<ClientEntity>  findByLastNameContainingIgnoreCase(String lastName);

    List<ClientEntity>findByFirstNameContainingIgnoreCaseAndLastNameContainingIgnoreCase(String firstName, String lastName);

    Optional<ClientEntity> findByEmail(String email);

    // Skontroluje, či už v databáze existuje klient s daným emailom.
    boolean existsByEmail(String email);
}
