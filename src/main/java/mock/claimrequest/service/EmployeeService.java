package mock.claimrequest.service;

import java.util.List;

import mock.claimrequest.dto.employee.EmployeeGetDTO;
import mock.claimrequest.dto.employee.EmployeeSaveDTO;

public interface EmployeeService {

    boolean saveEmployeeAlongWithAccount(EmployeeSaveDTO employeeSaveDTO);
    List<EmployeeGetDTO> getAll();
}
