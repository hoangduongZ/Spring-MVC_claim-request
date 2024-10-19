package mock.claimrequest.service;

import java.util.List;
import java.util.UUID;

import mock.claimrequest.dto.employee.EmployeeSaveDTO;
import mock.claimrequest.dto.employeeProject.EmployeeProjectDTO;

public interface EmployeeService {

    boolean saveEmployeeAlongWithAccount(EmployeeSaveDTO employeeSaveDTO);
    List<EmployeeProjectDTO> getAllEmployeeFree();

    List<EmployeeProjectDTO> getAllEmployeeFreeAndWorkingCurrentProject(UUID projectId);
}
