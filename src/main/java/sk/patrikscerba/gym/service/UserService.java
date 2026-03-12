package sk.patrikscerba.gym.service;

import sk.patrikscerba.gym.dto.UserRegisterRequest;

/**
 * Rozhranie pre servisnú vrstvu používateľa.
 * Definuje operácie súvisiace s registráciou používateľa.
 */
public interface UserService {

    void  create (UserRegisterRequest request);



}
