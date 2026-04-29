package sk.patrikscerba.gym.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sk.patrikscerba.gym.entity.EntryEntity;
import sk.patrikscerba.gym.entity.ClientEntity;
import sk.patrikscerba.gym.enums.EntryStatus;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Repository pre prácu so záznamami vstupov.
 * Slúži na ukladanie, načítanie a kontrolu údajov o vstupoch klientov.
 */
public interface EntryRepository extends JpaRepository<EntryEntity, Long> {

    // Overí, či už má klient v daný deň vytvorený vstup.
    boolean existsByClientAndArrivalTimeBetweenAndStatus(
            ClientEntity client,
            LocalDateTime startOfDay,
            LocalDateTime endOfDay,
            EntryStatus status);

    // Nájde posledný schválený vstup klienta bez zaznamenaného odchodu.
    Optional<EntryEntity> findTopByClientAndStatusAndDepartureTimeIsNullOrderByArrivalTimeDesc(
            ClientEntity client,
            EntryStatus status
    );

    // Overí, či klient má aktívny vstup (schválený a bez zaznamenaného odchodu).
    boolean existsByClientAndStatusAndDepartureTimeIsNull(
            ClientEntity client,
            EntryStatus status
    );

    // Nájde všetky aktuálne aktívne vstupy klientov v fitnescentre.
    List<EntryEntity> findByStatusAndDepartureTimeIsNullOrderByArrivalTimeDesc(
            EntryStatus status
    );
}



