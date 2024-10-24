package mock.claimrequest.dto.employee;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import mock.claimrequest.dto.auth.AccountGetDTO;
import mock.claimrequest.dto.auth.AccountRegisterDTO;
import mock.claimrequest.dto.department.DepartmentDTO;
import mock.claimrequest.entity.Account;
import mock.claimrequest.entity.Department;
import mock.claimrequest.entity.Role;
import mock.claimrequest.entity.entityEnum.AccountRole;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EmployeeUpdateDTO {
    private UUID id;
    private String firstname;
    private String lastname;
    private boolean gender;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate dob;
    private String address;
    private DepartmentDTO department;
    private AccountGetDTO accountDTO;
    private AccountRole role;
}
