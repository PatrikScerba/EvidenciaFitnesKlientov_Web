package sk.patrikscerba.gym.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import sk.patrikscerba.gym.entity.MembershipEntity;

/**
 * Repository pre prácu s permanentkami v databáze.
 */
public interface MembershipRepository extends JpaRepository<MembershipEntity, Long> {

    // Vyhľadanie permanentky podľa ID klienta.
    Optional<MembershipEntity> findByClient_ClientId(Long clientId);
}

