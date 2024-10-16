package mock.claimrequest.service;

import java.util.List;
import java.util.UUID;

import mock.claimrequest.dto.employee.EmployeeDTO;
import mock.claimrequest.dto.employee.EmployeeSaveDTO;

public interface EmployeeService {

    boolean saveEmployeeAlongWithAccount(EmployeeSaveDTO employeeSaveDTO);

    List<EmployeeDTO> getAllEmployees();
    EmployeeDTO getEmployeeById(UUID id);
    void saveEmployee(EmployeeDTO employeeDTO);
    void updateEmployee(UUID id, EmployeeDTO employeeDTO);
    void deleteEmployee(UUID id);
    
}
