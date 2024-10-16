package mock.claimrequest.service.impl;

import mock.claimrequest.dto.employee.EmployeeDTO;
import mock.claimrequest.dto.employee.EmployeeSaveDTO;
import mock.claimrequest.entity.Account;
import mock.claimrequest.entity.Department;
import mock.claimrequest.entity.Employee;
import mock.claimrequest.entity.Role;
import mock.claimrequest.entity.entityEnum.AccountStatus;
import mock.claimrequest.repository.AccountRepository;
import mock.claimrequest.repository.DepartmentRepository;
import mock.claimrequest.repository.EmployeeRepository;
import mock.claimrequest.repository.RoleRepository;
import mock.claimrequest.service.EmployeeService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Service
public class EmployeeServiceImpl implements EmployeeService {
    private final EmployeeRepository employeeRepository;
    private final AccountRepository accountRepository;
    private final DepartmentRepository departmentRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public EmployeeServiceImpl(EmployeeRepository employeeRepository, AccountRepository accountRepository, DepartmentRepository departmentRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        this.employeeRepository = employeeRepository;
        this.accountRepository = accountRepository;
        this.departmentRepository = departmentRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public List<Employee> getAll(){
        return employeeRepository.findAll();
    }

    @Override
    public boolean saveEmployeeAlongWithAccount(EmployeeSaveDTO employeeSaveDTO){
        if (employeeSaveDTO == null) {
            throw new IllegalStateException("Employee should be not null");
        }

        Employee employee = new Employee();
        employee.setDob(employeeSaveDTO.getDob());
        employee.setAddress(employeeSaveDTO.getAddress());
        employee.setFirstname(employeeSaveDTO.getFirstname());
        employee.setLastname(employeeSaveDTO.getLastname());
        employee.setGender(employeeSaveDTO.isGender());

        Account account = new Account();
        account.setEmail(employeeSaveDTO.getAccountRegisterDTO().getEmail());
        account.setPassword(passwordEncoder.encode(employeeSaveDTO.getAccountRegisterDTO().getPassword()));
        account.setUserName(employeeSaveDTO.getAccountRegisterDTO().getUserName());
        account.setStatus(AccountStatus.ACTIVE);
        account.setEmployee(employee);
        Set<Role> roles = new HashSet<>();
        roles.add(roleRepository.findByName("CLAIMER"));
        account.setRoles(roles);
        employee.setAccounts(account);

        Department department = departmentRepository.
                findById(employeeSaveDTO.getDepartmentId()).orElseThrow(
                        () -> new IllegalStateException("Department not existed"));
        employee.setDepartment(department);

        employeeRepository.save(employee);

        return employee.getId() != null;
    }


    @Override
    public List<EmployeeDTO> getAllEmployees() {
        return null;
    }

    @Override
    public EmployeeDTO getEmployeeById(UUID id) {
        return null;
    }

    @Override
    public void saveEmployee(EmployeeDTO employeeDTO) {

    }

    @Override
    public void updateEmployee(UUID id, EmployeeDTO employeeDTO) {

    }

    @Override
    public void deleteEmployee(UUID id) {

    }
}
