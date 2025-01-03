package mock.claimrequest.dto.project;

import jakarta.validation.Valid;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import mock.claimrequest.dto.employeeProject.EmployeeProjectDTO;
import mock.claimrequest.entity.entityEnum.ProjectStatus;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProjectDTO {
    @NotNull(message = "Project ID is required.")
    private UUID id;

    @NotBlank(message = "Project name is required.")
    @Size(min = 3, max = 100, message = "Project name must be between 3 and 100 characters.")
    private String name;

    @Size(max = 500, message = "Description must not exceed 500 characters.")
    private String description;

    @NotNull(message = "Start date is required.")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate startDate;

    @NotNull(message = "End date is required.")
    @Future(message = "End date must be in the future.")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate endDate;

    @NotNull(message = "Budget is required.")
    @DecimalMin(value = "0.0", inclusive = false, message = "Budget must be greater than 0.")
    @Digits(integer = 10, fraction = 2, message = "Budget must be a valid monetary amount.")
    private BigDecimal budget;

    @NotNull(message = "Project status is required.")
    private ProjectStatus status;

    private List<@Valid EmployeeProjectDTO> employeeProjects;
}
