package mock.claimrequest.service.impl;

import mock.claimrequest.dto.auth.AccountRegisterDTO;
import mock.claimrequest.entity.Account;
import mock.claimrequest.entity.Employee;
import mock.claimrequest.entity.entityEnum.AccountRole;
import mock.claimrequest.entity.entityEnum.AccountStatus;
import mock.claimrequest.entity.Role;
import mock.claimrequest.repository.AccountRepository;
import mock.claimrequest.repository.EmployeeRepository;
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
    private final EmployeeRepository employeeRepository;

    public AccountServiceImpl(AccountRepository accountRepository, ModelMapper modelMapper, PasswordEncoder passwordEncoder, RoleRepository roleRepository, EmployeeRepository employeeRepository) {
        this.accountRepository = accountRepository;
        this.modelMapper = modelMapper;
        this.passwordEncoder = passwordEncoder;
        this.roleRepository = roleRepository;
        this.employeeRepository = employeeRepository;
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
        if (!roleRepository.existsByName(AccountRole.ADMIN)){
            Role role = new Role();
            role.setName(AccountRole.ADMIN);
            roleRepository.save(role);
            roles.add(role);
        }else{
            roles.add(roleRepository.findByName(AccountRole.ADMIN).get());
        }
        Employee employee = new Employee();
        employeeRepository.save(employee);

        account.setRoles(roles);
        account.setEmployee(employee);
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
