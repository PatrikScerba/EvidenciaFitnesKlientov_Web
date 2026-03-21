package sk.patrikscerba.gym.service.user;

import sk.patrikscerba.gym.dto.auth.UserRegisterRequest;

/**
 * Rozhranie pre servisnú vrstvu používateľa.
 * Definuje operácie súvisiace s registráciou používateľa.
 */
public interface UserService {

    void  create (UserRegisterRequest request);



}
