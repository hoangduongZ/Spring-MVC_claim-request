package mock.claimrequest.service;

import mock.claimrequest.dto.employee.EmployeeListDTO;
import mock.claimrequest.dto.employee.EmployeeSaveDTO;
import mock.claimrequest.dto.employeeProject.EmployeeProjectDTO;
import mock.claimrequest.entity.Account;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface EmployeeService {
    boolean saveEmployeeAlongWithAccount(EmployeeSaveDTO employeeSaveDTO);
    List<EmployeeProjectDTO> getAllEmployeeFree();

    List<EmployeeProjectDTO> getAllEmployeeFreeAndWorkingCurrentProject(UUID projectId);

    List<EmployeeListDTO> getAll();
}
