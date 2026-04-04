package sk.patrikscerba.gym.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sk.patrikscerba.gym.entity.UserEntity;

import java.util.Optional;

/**
 * Repository pre prácu s používateľmi v databáze.
 */
public interface UserRepository extends JpaRepository<UserEntity, Long> {

    boolean existsByEmail(String email);

    Optional<UserEntity> findByEmail(String email);

    Optional<UserEntity> findByClient_ClientId(Long clientId);
}
