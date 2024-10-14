package mock.claimrequest.service;

import java.util.List;
import java.util.UUID;

import mock.claimrequest.dto.employee.EmployeeDTO;
import mock.claimrequest.entity.Employee;

public interface EmployeeService {
    
    List<EmployeeDTO> getAllEmployees();
    Employee getEmployeeById(UUID id);
    void saveEmployee(EmployeeDTO employeeDTO);
    void updateEmployee(UUID id, EmployeeDTO employeeDTO);
    void deleteEmployee(UUID id);
    
}
