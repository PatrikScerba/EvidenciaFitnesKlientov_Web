package sk.patrikscerba.gym.service;

import sk.patrikscerba.gym.dto.EmployeeAccountResponse;
import sk.patrikscerba.gym.dto.EmployeeCreateRequest;

/**
 * Servisné rozhranie pre vytvorenie účtu zamestnanca.
 */
public interface EmployeeAccountService {

    EmployeeAccountResponse create (EmployeeCreateRequest request);
}

