package mock.claimrequest.service.impl;

import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import mock.claimrequest.entity.Account;
import mock.claimrequest.repository.AccountRepository;
import mock.claimrequest.service.AccountService;


@Service
public class AccountServiceImpl implements AccountService {
     

    @Autowired
    private AccountRepository accountRepository;

    @Override
    public Optional<Account> getAccountsByEmployeeId(UUID employeeId) {
        return accountRepository.findByEmployeeId(employeeId);

    }
    

 
    
}
