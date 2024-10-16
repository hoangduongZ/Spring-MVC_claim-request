package mock.claimrequest.service;

import mock.claimrequest.dto.employee.EmployeeGetDTO;
import mock.claimrequest.dto.employee.EmployeeSaveDTO;

import java.util.List;

public interface EmployeeService {
    boolean saveEmployeeAlongWithAccount(EmployeeSaveDTO employeeSaveDTO);
    List<EmployeeGetDTO> getAll();
}
