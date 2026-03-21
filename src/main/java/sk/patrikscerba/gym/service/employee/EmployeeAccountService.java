package sk.patrikscerba.gym.service.employee;

import sk.patrikscerba.gym.dto.employee.EmployeeAccountResponse;
import sk.patrikscerba.gym.dto.employee.EmployeeCreateRequest;

/**
 * Servisné rozhranie pre vytvorenie účtu zamestnanca.
 */
public interface EmployeeAccountService {

    EmployeeAccountResponse create (EmployeeCreateRequest request);
}

