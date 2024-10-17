package mock.claimrequest.service;

import mock.claimrequest.dto.employee.EmployeeSaveDTO;
import mock.claimrequest.dto.employeeProject.EmployeeProjectDTO;

import java.util.List;
import java.util.UUID;

public interface EmployeeService {
    boolean saveEmployeeAlongWithAccount(EmployeeSaveDTO employeeSaveDTO);
    List<EmployeeProjectDTO> getAllEmployeeFree();

    List<EmployeeProjectDTO> getAllEmployeeFreeAndWorkingCurrentProject(UUID projectId);
}
