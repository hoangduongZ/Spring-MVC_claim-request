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
public class EmployeeForProjectSaveDTO {
    private UUID employeeId;
    private ProjectRole role;
    private String accountName;
}
