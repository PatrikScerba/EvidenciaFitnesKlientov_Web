package sk.patrikscerba.gym.service.client;

import sk.patrikscerba.gym.dto.client.ClientAccountResponse;
import sk.patrikscerba.gym.dto.client.ClientCreateRequest;

/**
 * Servisné rozhranie pre registráciu klienta spolu s vytvorením používateľského účtu.
 */
public interface ClientAccountService {

    ClientAccountResponse registerClientWithAccount(ClientCreateRequest request);
}