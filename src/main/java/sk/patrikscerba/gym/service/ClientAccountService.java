package sk.patrikscerba.gym.service;

import sk.patrikscerba.gym.dto.ClientAccountResponse;
import sk.patrikscerba.gym.dto.ClientCreateRequest;

/**
 * Servisné rozhranie pre registráciu klienta spolu s vytvorením používateľského účtu.
 */
public interface ClientAccountService {

    ClientAccountResponse registerClientWithAccount(ClientCreateRequest request);
}