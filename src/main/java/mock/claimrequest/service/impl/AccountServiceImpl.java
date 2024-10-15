package mock.claimrequest.service.impl;

import mock.claimrequest.dto.auth.AccountRegisterDTO;
import mock.claimrequest.entity.Account;
import mock.claimrequest.entity.AccountStatus;
import mock.claimrequest.repository.AccountRepository;
import mock.claimrequest.service.AccountService;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AccountServiceImpl implements AccountService {
    private final AccountRepository accountRepository;
    private ModelMapper modelMapper;
    private PasswordEncoder passwordEncoder;

    public AccountServiceImpl(AccountRepository accountRepository, ModelMapper modelMapper, PasswordEncoder passwordEncoder) {
        this.accountRepository = accountRepository;
        this.modelMapper = modelMapper;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public boolean register(AccountRegisterDTO accountRegisterDTO){
        if (accountRegisterDTO == null){
            throw new IllegalStateException("Account not null");
        }

        Account account= modelMapper.map(accountRegisterDTO, Account.class);
        account.setPassword(passwordEncoder.encode(account.getPassword()));
        account.setStatus(AccountStatus.ACTIVE);
        accountRepository.save(account);
        return account.getId() != null;
    }

    @Override
    public boolean existByEmail(String email){
        return accountRepository.existsByEmail(email);
    }

    @Override
    public boolean existByUsername(String username) {
        return accountRepository.existsByUserName(username);
    }
}
