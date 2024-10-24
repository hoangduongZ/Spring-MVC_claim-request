package mock.claimrequest.dto.project;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import mock.claimrequest.dto.employeeProject.EmployeeProjectDTO;
import mock.claimrequest.entity.entityEnum.ProjectStatus;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProjectSaveDTO {
    private String name;

    private String description;

    private LocalDate startDate;

    private LocalDate endDate;

    private BigDecimal budget;

    private List<EmployeeProjectDTO> employeeProjects;
}
