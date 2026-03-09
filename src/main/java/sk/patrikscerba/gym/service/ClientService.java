package sk.patrikscerba.gym.service;

import sk.patrikscerba.gym.dto.ClientCreateRequest;
import sk.patrikscerba.gym.dto.ClientResponse;
import sk.patrikscerba.gym.dto.ClientUpdateRequest;

import java.util.List;

/**
 * Service rozhranie pre prácu s klientmi.
 * Definuje základné operácie, ktoré bude implementovať service vrstva.
 */
public interface ClientService {

    ClientResponse registerClient(ClientCreateRequest request);

    ClientResponse getClientById(Long id);

    List<ClientResponse> getAllClients();

    ClientResponse updateClient(Long id, ClientUpdateRequest request);

    void deleteClient(Long id);



}
