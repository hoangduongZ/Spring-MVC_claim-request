package mock.claimrequest.service;

import mock.claimrequest.entity.Employee;
import mock.claimrequest.entity.EmployeeProject;
import mock.claimrequest.entity.entityEnum.ProjectRole;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface EmployeeProjectService {
    List<EmployeeProject> getAll();
    ProjectRole getRoleInProject(UUID employeeId, UUID projectId);
    Employee findProjectManager(UUID projectId);
}
