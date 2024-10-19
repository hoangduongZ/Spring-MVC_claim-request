package mock.claimrequest.service.impl;

import mock.claimrequest.entity.EmployeeProject;
import mock.claimrequest.entity.entityEnum.ProjectRole;
import mock.claimrequest.repository.EmployeeProjectRepository;
import mock.claimrequest.service.EmployeeProjectService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class EmployeeProjectServiceImpl implements EmployeeProjectService {
    private final EmployeeProjectRepository employeeProjectRepository;

    public EmployeeProjectServiceImpl(EmployeeProjectRepository employeeProjectRepository) {
        this.employeeProjectRepository = employeeProjectRepository;
    }

    @Override
    public List<EmployeeProject> getAll() {
        return employeeProjectRepository.findAll();
    }

    public Optional<ProjectRole> getRoleInProject(UUID employeeId, UUID projectId) {
        return employeeProjectRepository.findByEmployeeIdAndProjectId(employeeId, projectId)
                .map(EmployeeProject::getRole);
    }
}
