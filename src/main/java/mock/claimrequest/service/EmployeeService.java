package mock.claimrequest.service;

import jakarta.validation.Valid;
import mock.claimrequest.dto.employee.EmployeeListDTO;
import mock.claimrequest.dto.employee.EmployeeSaveDTO;
import mock.claimrequest.dto.employee.EmployeeUpdateDTO;
import mock.claimrequest.dto.employeeProject.EmployeeProjectDTO;
import mock.claimrequest.entity.Account;
import mock.claimrequest.entity.EmployeeProject;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface EmployeeService {
    boolean saveEmployeeAlongWithAccount(EmployeeSaveDTO employeeSaveDTO);
    List<EmployeeProjectDTO> getAllEmployeeFree();

    List<EmployeeProjectDTO> getAllEmployeeFreeAndWorkingCurrentProject(UUID projectId);

    List<EmployeeProject> getAllEmployeesExitedFromProject(UUID projectId);

    List<EmployeeListDTO> getAll();

    EmployeeUpdateDTO findById(UUID id);

    void updateEmployee(UUID id, @Valid EmployeeUpdateDTO employeeUpdateDTO);
}
