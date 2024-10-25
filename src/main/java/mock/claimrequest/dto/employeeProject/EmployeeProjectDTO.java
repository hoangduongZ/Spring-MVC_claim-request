package mock.claimrequest.dto.employeeProject;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import mock.claimrequest.entity.entityEnum.EmployeeStatus;
import mock.claimrequest.entity.entityEnum.ProjectRole;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeProjectDTO {
    @NotNull(message = "Employee ID is required.")
    private UUID employeeId;

    @NotBlank(message = "Account name is required.")
    @Size(min = 3, max = 50, message = "Account name must be between 3 and 50 characters.")
    private String accountName;

    private ProjectRole role;

    private EmployeeStatus employeeStatus;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
//    @FutureOrPresent(message = "Start date cannot be in the past.")
    private LocalDate startDate;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
//    @Future(message = "End date must be in the future.")
    private LocalDate endDate;
}
