package mock.claimrequest.dto.employee;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import mock.claimrequest.dto.auth.AccountRegisterDTO;
import mock.claimrequest.dto.department.DepartmentDTO;
import mock.claimrequest.entity.entityEnum.AccountRole;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EmployeeSaveDTO {
    private String firstname;
    private String lastname;
    private boolean gender;
    private LocalDate dob;
    private String address;
    private long departmentId;
    private AccountRegisterDTO accountRegisterDTO;
    private AccountRole accountRole;
}
