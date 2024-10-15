package mock.claimrequest.service.impl;

import mock.claimrequest.dto.employee.EmployeeForProjectSaveDto;
import mock.claimrequest.entity.Employee;
import mock.claimrequest.repository.EmployeeRepository;
import mock.claimrequest.service.EmployeeService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmployeeServiceImpl implements EmployeeService {
    private final EmployeeRepository employeeRepository;

    public EmployeeServiceImpl(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    public List<EmployeeForProjectSaveDto> getAll(){
        return employeeRepository.findAll().stream().map(
                employee -> new EmployeeForProjectSaveDto(employee.getId(), employee.getAccounts().getUserName())).toList();
    }

}
