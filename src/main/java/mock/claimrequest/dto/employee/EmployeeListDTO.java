package mock.claimrequest.dto.employee;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import mock.claimrequest.dto.auth.AccountGetDTO;
import mock.claimrequest.dto.department.DepartmentDTO;

import java.util.UUID;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeListDTO {
    private UUID id;
    private String fullName;
    private DepartmentDTO department;
    private AccountGetDTO account;
}
