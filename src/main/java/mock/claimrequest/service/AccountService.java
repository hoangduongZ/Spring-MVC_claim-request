package mock.claimrequest.service;

import java.util.Optional;
import java.util.UUID;

import mock.claimrequest.entity.Account;

public interface AccountService {
    Optional<Account> getAccountsByEmployeeId(UUID employeeId);
    
}
