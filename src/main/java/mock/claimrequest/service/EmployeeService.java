package mock.claimrequest.service;

import mock.claimrequest.dto.employee.EmployeeForProjectSaveDTO;
import mock.claimrequest.dto.employee.EmployeeSaveDTO;

import java.util.List;

public interface EmployeeService {
    boolean saveEmployeeAlongWithAccount(EmployeeSaveDTO employeeSaveDTO);
    List<EmployeeForProjectSaveDTO> getAll();
}
