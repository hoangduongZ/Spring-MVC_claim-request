package mock.claimrequest.service;

import mock.claimrequest.entity.EmployeeProject;
import mock.claimrequest.entity.entityEnum.ProjectRole;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface EmployeeProjectService {
    List<EmployeeProject> getAll();
    Optional<ProjectRole> getRoleInProject(UUID employeeId, UUID projectId);
}
