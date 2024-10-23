package mock.claimrequest.dto.employee;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import mock.claimrequest.dto.auth.AccountGetDTO;
import mock.claimrequest.dto.auth.AccountRegisterDTO;
import mock.claimrequest.entity.entityEnum.AccountRole;

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
    private LocalDate dob;
    private String address;
    private long departmentId;
    private AccountGetDTO accountGetDTO;
    private AccountRole accountRole;
}
