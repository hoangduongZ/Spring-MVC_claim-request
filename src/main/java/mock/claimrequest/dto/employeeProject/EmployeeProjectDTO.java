package mock.claimrequest.dto.employeeProject;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import mock.claimrequest.entity.entityEnum.ProjectRole;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeProjectDTO {
    private UUID employeeId;
    private String accountName;
    private ProjectRole role;
}
