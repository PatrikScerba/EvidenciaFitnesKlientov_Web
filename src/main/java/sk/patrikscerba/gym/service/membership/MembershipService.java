package sk.patrikscerba.gym.service.membership;

import sk.patrikscerba.gym.dto.membership.MembershipCreateRequest;
import sk.patrikscerba.gym.dto.membership.MembershipResponse;

/**
 * Service pre logiku správy permanentiek.
 */
public interface MembershipService {

    MembershipResponse createOrExtendMembership(MembershipCreateRequest request);

    MembershipResponse getMembershipByClientId(Long clientId);

    MembershipResponse getCurrentClientMembership(String email);
}


