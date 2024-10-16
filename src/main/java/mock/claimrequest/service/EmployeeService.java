package mock.claimrequest.service;

import mock.claimrequest.dto.employee.EmployeeSaveDTO;

public interface EmployeeService {
    boolean saveEmployeeAlongWithAccount(EmployeeSaveDTO employeeSaveDTO);
}
