package mock.claimrequest.dto.project;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import mock.claimrequest.entity.entityEnum.ProjectRole;
import mock.claimrequest.entity.entityEnum.ProjectStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProjectSaveDTO {
    private String name;

    private LocalDateTime startDate;

    private LocalDateTime endDate;

    private BigDecimal budget;

    private ProjectStatus projectStatus;

    private UUID employeeId;

    private ProjectRole role;
}
