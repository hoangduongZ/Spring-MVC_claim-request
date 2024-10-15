package mock.claimrequest.service.impl;

import mock.claimrequest.dto.auth.AccountRegisterDTO;
import mock.claimrequest.entity.Account;
import mock.claimrequest.entity.AccountStatus;
import mock.claimrequest.entity.Role;
import mock.claimrequest.repository.AccountRepository;
import mock.claimrequest.repository.RoleRepository;
import mock.claimrequest.service.AccountService;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
public class AccountServiceImpl implements AccountService {
    private final AccountRepository accountRepository;
    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;

    public AccountServiceImpl(AccountRepository accountRepository, ModelMapper modelMapper, PasswordEncoder passwordEncoder, RoleRepository roleRepository) {
        this.accountRepository = accountRepository;
        this.modelMapper = modelMapper;
        this.passwordEncoder = passwordEncoder;
        this.roleRepository = roleRepository;
    }

    @Override
    public boolean register(AccountRegisterDTO accountRegisterDTO){
        if (accountRegisterDTO == null){
            throw new IllegalStateException("Account not null");
        }

        Account account= modelMapper.map(accountRegisterDTO, Account.class);
        account.setPassword(passwordEncoder.encode(account.getPassword()));
        account.setStatus(AccountStatus.ACTIVE);

        Set<Role> roles= new HashSet<>();
        if (!roleRepository.existsByName("CLAIMER")){
            Role role = new Role();
            role.setName("CLAIMER");
            roleRepository.save(role);
            roles.add(role);
        }else{
            roles.add(roleRepository.findByName("CLAIMER"));
        }
        account.setRoles(roles);
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
