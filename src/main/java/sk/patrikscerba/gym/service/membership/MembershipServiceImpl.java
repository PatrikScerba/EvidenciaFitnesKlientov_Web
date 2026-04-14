package sk.patrikscerba.gym.service.membership;

import org.springframework.stereotype.Service;
import sk.patrikscerba.gym.dto.membership.MembershipCreateRequest;
import sk.patrikscerba.gym.dto.membership.MembershipResponse;
import sk.patrikscerba.gym.entity.ClientEntity;
import sk.patrikscerba.gym.entity.MembershipEntity;
import sk.patrikscerba.gym.entity.UserEntity;
import sk.patrikscerba.gym.enums.MembershipStatus;
import sk.patrikscerba.gym.exception.BusinessException;
import sk.patrikscerba.gym.exception.NotFoundException;
import sk.patrikscerba.gym.repository.ClientRepository;
import sk.patrikscerba.gym.repository.MembershipRepository;
import sk.patrikscerba.gym.repository.UserRepository;

import java.time.LocalDate;

/**
 * Implementácia service logiky pre správu permanentiek.
 * Zabezpečuje vytvorenie a obnovu permanentky,
 * jej načítanie a zistenie aktuálneho stavu.
 */
@Service
public class MembershipServiceImpl implements MembershipService {

    private final MembershipRepository membershipRepository;
    private final ClientRepository clientRepository;
    private final UserRepository userRepository;

    public MembershipServiceImpl(
            MembershipRepository membershipRepository,
            ClientRepository clientRepository,
            UserRepository userRepository
    ) {
        this.membershipRepository = membershipRepository;
        this.clientRepository = clientRepository;
        this.userRepository = userRepository;
    }

    // Vytvorí novú permanentku klientovi alebo obnoví permanentku po jej expirácii.
    @Override
    public MembershipResponse createOrExtendMembership(MembershipCreateRequest request) {

        ClientEntity client = clientRepository.findById(request.getClientId())
                .orElseThrow(() -> new NotFoundException("Klient s daným ID neexistuje."));

        // Skúsi načítať existujúcu permanentku klienta, inak vytvorí novú entitu.
        MembershipEntity membership = membershipRepository.findByClient_ClientId(client.getClientId())
                .orElseGet(MembershipEntity::new);

        // Ak už klient má aktívnu permanentku, nie je možné ju znova vytvoriť alebo obnoviť.
        if (membership.getId() != null && !membership.getValidTo().isBefore(LocalDate.now())) {
            throw new BusinessException("Klient už má aktívnu permanentku.");
        }

        // Nová alebo obnovená permanentka začne platiť od aktuálneho dňa.
        LocalDate validFrom = LocalDate.now();

        // Dátum konca platnosti sa vypočíta podľa zvolenej dĺžky permanentky.
        LocalDate validTo = validFrom.plusDays(request.getDuration().getDays() - 1L);

        membership.setClient(client);
        membership.setValidFrom(validFrom);
        membership.setValidTo(validTo);
        membership.setStatus(calculateStatus(validTo));

        MembershipEntity savedMembership = membershipRepository.save(membership);

        return mapToResponse(savedMembership);
    }

    // Získanie permanentky podľa ID klienta.
    @Override
    public MembershipResponse getMembershipByClientId(Long clientId) {
        MembershipEntity membership = membershipRepository.findByClient_ClientId(clientId)
                .orElseThrow(() -> new NotFoundException("Klient nemá žiadnu permanentku."));

        // Prepočet aktuálneho stavu permanentky podľa dátumu platnosti.
        MembershipStatus currentStatus = calculateStatus(membership.getValidTo());

        if (membership.getStatus() != currentStatus) {
            membership.setStatus(currentStatus);
            membership = membershipRepository.save(membership);
        }

        return mapToResponse(membership);
    }

    // Získanie permanentky aktuálne prihláseného klienta podľa emailu.
    @Override
    public MembershipResponse getCurrentClientMembership(String email) {
        UserEntity user = userRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException("Používateľ s daným emailom neexistuje."));

        if (user.getClient() == null) {
            throw new NotFoundException("Prihlásený používateľ nemá priradeného klienta.");
        }

        return getMembershipByClientId(user.getClient().getClientId());
    }

    // Určí stav permanentky podľa dátumu jej platnosti.
    private MembershipStatus calculateStatus(LocalDate validTo) {
        if (validTo.isBefore(LocalDate.now())) {
            return MembershipStatus.EXPIRED;
        }
        return MembershipStatus.ACTIVE;
    }

    // Prevedie MembershipEntity na MembershipResponse.
    private MembershipResponse mapToResponse(MembershipEntity membership) {
        MembershipResponse response = new MembershipResponse();
        response.setId(membership.getId());
        response.setClientId(membership.getClient().getClientId());
        response.setStatus(membership.getStatus());
        response.setValidFrom(membership.getValidFrom());
        response.setValidTo(membership.getValidTo());
        return response;
    }
}

