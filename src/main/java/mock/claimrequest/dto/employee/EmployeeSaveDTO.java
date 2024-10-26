package mock.claimrequest.dto.employee;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;
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
    @NotBlank(message = "First name is required.")
    @Size(min = 2, max = 50, message = "First name must be between 2 and 50 characters.")
    private String firstname;

    @NotBlank(message = "Last name is required.")
    @Size(min = 2, max = 50, message = "Last name must be between 2 and 50 characters.")
    private String lastname;

    private boolean gender;

    @NotNull(message = "Date of birth is required.")
    @Past(message = "Date of birth must be in the past.")
    private LocalDate dob;

    @NotBlank(message = "Address is required.")
    @Size(max = 255, message = "Address must not exceed 255 characters.")
    private String address;

    @Min(value = 1, message = "Department ID must be a positive number.")
    private long departmentId;

    @NotNull(message = "Account registration information is required.")
    private AccountRegisterDTO accountRegisterDTO;

    @NotNull(message = "Account role is required.")
    private AccountRole accountRole;
}
