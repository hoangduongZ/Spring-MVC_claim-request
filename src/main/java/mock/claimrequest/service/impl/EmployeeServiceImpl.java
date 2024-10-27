package mock.claimrequest.service.impl;

import mock.claimrequest.dto.auth.AccountDTO;
import mock.claimrequest.dto.department.DepartmentDTO;
import mock.claimrequest.dto.employee.EmployeeListDTO;
import mock.claimrequest.dto.employee.EmployeeSaveDTO;
import mock.claimrequest.dto.employee.EmployeeUpdateDTO;
import mock.claimrequest.dto.employeeProject.EmployeeProjectDTO;
import mock.claimrequest.entity.Account;
import mock.claimrequest.entity.Department;
import mock.claimrequest.entity.Employee;
import mock.claimrequest.entity.Role;
import mock.claimrequest.entity.entityEnum.AccountRole;
import mock.claimrequest.entity.entityEnum.AccountStatus;
import mock.claimrequest.entity.entityEnum.EmpProjectStatus;
import mock.claimrequest.entity.entityEnum.EmployeeStatus;
import mock.claimrequest.repository.AccountRepository;
import mock.claimrequest.repository.DepartmentRepository;
import mock.claimrequest.repository.EmployeeRepository;
import mock.claimrequest.repository.RoleRepository;
import mock.claimrequest.service.EmployeeService;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

@Service
public class EmployeeServiceImpl implements EmployeeService {
    private final EmployeeRepository employeeRepository;
    private final AccountRepository accountRepository;
    private final DepartmentRepository departmentRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final ModelMapper modelMapper;

    public EmployeeServiceImpl(EmployeeRepository employeeRepository, AccountRepository accountRepository, DepartmentRepository departmentRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder, ModelMapper modelMapper) {
        this.employeeRepository = employeeRepository;
        this.accountRepository = accountRepository;
        this.departmentRepository = departmentRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.modelMapper = modelMapper;
    }

    @Override
    public boolean saveEmployeeAlongWithAccount(EmployeeSaveDTO employeeSaveDTO) {
        if (employeeSaveDTO == null) {
            throw new IllegalStateException("Employee should be not null");
        }

        Employee employee = new Employee();
        employee.setDob(employeeSaveDTO.getDob());
        employee.setAddress(employeeSaveDTO.getAddress());
        employee.setFirstname(employeeSaveDTO.getFirstname());
        employee.setLastname(employeeSaveDTO.getLastname());
        employee.setGender(employeeSaveDTO.isGender());
        if(employeeSaveDTO.getAccountRole().equals(AccountRole.FINANCE) ||employeeSaveDTO.getAccountRole().equals(AccountRole.ADMIN)){
            employee.setEmployeeStatus(null);
        }else{
            employee.setEmployeeStatus(EmployeeStatus.FREE);
        }
        Account account = new Account();
        account.setEmail(employeeSaveDTO.getAccountRegisterDTO().getEmail());
        account.setPassword(passwordEncoder.encode(employeeSaveDTO.getAccountRegisterDTO().getPassword()));
        account.setUserName(employeeSaveDTO.getAccountRegisterDTO().getUserName());
        account.setStatus(AccountStatus.ACTIVE);
        account.setEmployee(employee);

        AccountRole roleName = employeeSaveDTO.getAccountRole();
        Role role = roleRepository.findByName(roleName)
                .orElseGet(() -> {
                    Role newRole = new Role();
                    newRole.setName(roleName);
                    return roleRepository.save(newRole);
                });

        Set<Role> roles = new HashSet<>();
        roles.add(role);
        account.setRoles(roles);
        employee.setAccount(account);

        Department department = departmentRepository.
                findById(employeeSaveDTO.getDepartmentId()).orElseThrow(
                        () -> new IllegalStateException("Department not existed"));
        employee.setDepartment(department);

        employeeRepository.save(employee);

        return employee.getId() != null;
    }

    @Override
    public List<EmployeeProjectDTO> getAllEmployeeFree() {
        return employeeRepository.findByEmployeeStatus(EmployeeStatus.FREE).stream().map(
                employee -> {
                    EmployeeProjectDTO saveDTO = new EmployeeProjectDTO();
                    if (employee.getAccount()!=null){
                        saveDTO.setAccountName(employee.getAccount().getUserName());
                    }
                    saveDTO.setEmployeeId(employee.getId());
                    saveDTO.setEmployeeStatus(employee.getEmployeeStatus());
                    return saveDTO;
                }).toList();
    }

    @Override
    public List<EmployeeProjectDTO> getAllEmployeeFreeAndWorkingCurrentProject(UUID projectId) {
        return employeeRepository.findAllFreeOrWorkingInProject(projectId,
                EmployeeStatus.FREE, EmployeeStatus.WORKING, EmpProjectStatus.IN).stream().map(
                employee -> {
                    EmployeeProjectDTO saveDTO = new EmployeeProjectDTO();
                    saveDTO.setAccountName(employee.getAccount().getUserName());
                    saveDTO.setEmployeeId(employee.getId());
                    saveDTO.setEmployeeStatus(employee.getEmployeeStatus());
                    return saveDTO;
                }).toList();
    }

    @Override
    public List<EmployeeListDTO> getAll() {
        List<Account> nonAdminAccounts = accountRepository.findAllNonAdminAccounts();

        return nonAdminAccounts.stream().map(account -> {
            Employee employee = account.getEmployee();
            EmployeeListDTO employeeListDTO = new EmployeeListDTO();

            employeeListDTO.setFullName(employee.getFirstname() + " " + employee.getLastname());

            Set<Role> roles = account.getRoles();
            AccountDTO accountDTO = new AccountDTO();
            accountDTO.setRoles(roles);
            accountDTO.setUserName(account.getUserName());
            accountDTO.setEmail(account.getEmail());
            accountDTO.setId(account.getId());
            employeeListDTO.setAccount(accountDTO);
            employeeListDTO.setAccount(accountDTO);
            employeeListDTO.setId(employee.getId());

            DepartmentDTO departmentDTO = modelMapper.map(employee.getDepartment(), DepartmentDTO.class);
            employeeListDTO.setDepartment(departmentDTO);

            return employeeListDTO;
        }).toList();
    }

    @Override
    public EmployeeUpdateDTO findById(UUID id) {
        Employee employee = employeeRepository.findById(id).orElseThrow(
                ()-> new IllegalStateException("Employee not existed"));
        EmployeeUpdateDTO employeeUpdateDTO = new EmployeeUpdateDTO();
        employeeUpdateDTO.setId(employee.getId());
        employeeUpdateDTO.setFirstname(employee.getFirstname());
        employeeUpdateDTO.setLastname(employee.getLastname());
        employeeUpdateDTO.setAddress(employee.getAddress());
        employeeUpdateDTO.setDob(employee.getDob());
        employeeUpdateDTO.setGender(employee.isGender());
        DepartmentDTO departmentDTO = modelMapper.map(employee.getDepartment(), DepartmentDTO.class);
        employeeUpdateDTO.setDepartment(departmentDTO);
        AccountDTO accountDTO = modelMapper.map(employee.getAccount(), AccountDTO.class);
        employeeUpdateDTO.setAccountDTO(accountDTO);
        AccountRole accountRole= Objects.requireNonNull(employee.getAccount().getRoles().stream().findFirst().orElse(null)).getName();
        employeeUpdateDTO.setRole(accountRole);
        return employeeUpdateDTO;
    }

    public void updateEmployee(UUID id, EmployeeUpdateDTO employeeUpdateDTO) {
        Employee employee = employeeRepository.findById(id).orElseThrow(
                () -> new IllegalStateException("Employee not found")
        );

        employee.setFirstname(employeeUpdateDTO.getFirstname());
        employee.setLastname(employeeUpdateDTO.getLastname());
        employee.setAddress(employeeUpdateDTO.getAddress());
        employee.setDob(employeeUpdateDTO.getDob());
        employee.setGender(employeeUpdateDTO.isGender());

        if(employeeUpdateDTO.getRole().equals(AccountRole.FINANCE) || employeeUpdateDTO.getRole().equals(AccountRole.ADMIN)){
            employee.setEmployeeStatus(null);
        }else{
            employee.setEmployeeStatus(EmployeeStatus.FREE);
        }


        if (employeeUpdateDTO.getDepartment() != null) {
            Department department = modelMapper.map(employeeUpdateDTO.getDepartment(), Department.class);
            employee.setDepartment(department);
        }

        Account account = employee.getAccount();
        if (account != null) {
            account.setUserName(employeeUpdateDTO.getAccountDTO().getUserName());
            account.setEmail(employeeUpdateDTO.getAccountDTO().getEmail());

            Role role= roleRepository.findByName(employeeUpdateDTO.getRole()).get();
            Set<Role> roles = new HashSet<>();
            role.setName(role.getName());
            roles.add(role);
            account.setRoles(roles);
        }

        employeeRepository.save(employee);
    }


}
