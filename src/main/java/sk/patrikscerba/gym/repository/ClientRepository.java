package sk.patrikscerba.gym.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sk.patrikscerba.gym.entity.ClientEntity;

/**
 * Repository vrstva pre prácu s entitou klienta.
 * Poskytuje základné CRUD operácie nad entitou ClientEntity.
 */
public interface ClientRepository extends JpaRepository<ClientEntity,Long> {

    // Skontroluje, či už v databáze existuje klient s daným emailom.
    boolean existsByEmail(String email);
}
