package mock.claimrequest.service.impl;

import mock.claimrequest.dto.employeeProject.EmployeeForProjectSaveDTO;
import mock.claimrequest.dto.project.ProjectSaveDTO;
import mock.claimrequest.entity.Employee;
import mock.claimrequest.entity.EmployeeProject;
import mock.claimrequest.entity.EmployeeProjectId;
import mock.claimrequest.entity.Project;
import mock.claimrequest.entity.entityEnum.ProjectStatus;
import mock.claimrequest.repository.EmployeeProjectRepository;
import mock.claimrequest.repository.EmployeeRepository;
import mock.claimrequest.repository.ProjectRepository;
import mock.claimrequest.service.ProjectService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Transactional
@Service
public class ProjectServiceImpl implements ProjectService {
    private final ProjectRepository projectRepository;
    private final EmployeeProjectRepository employeeProjectRepository;
    private final EmployeeRepository employeeRepository;

    public ProjectServiceImpl(ProjectRepository projectRepository, EmployeeProjectRepository employeeProjectRepository, EmployeeRepository employeeRepository) {
        this.projectRepository = projectRepository;
        this.employeeProjectRepository = employeeProjectRepository;
        this.employeeRepository = employeeRepository;
    }

    @Override
    public void create(ProjectSaveDTO projectSaveDTO) {
        Project project = new Project();
        project.setName(projectSaveDTO.getName());
        project.setDescription(projectSaveDTO.getDescription());
        project.setStartDate(projectSaveDTO.getStartDate());
        project.setEndDate(projectSaveDTO.getEndDate());
        project.setBudget(projectSaveDTO.getBudget());

        if (projectSaveDTO.getProjectStatus() == null || projectSaveDTO.getStartDate().isEqual(LocalDate.now())) {
            project.setProjectStatus(ProjectStatus.IN_PROGRESS);
        } else {
            project.setProjectStatus(ProjectStatus.NOT_STARTED);
        }

        projectRepository.save(project);

        List<EmployeeProject> employeeProjects = projectSaveDTO.getEmployeeProjects().stream()
                .map(employeeForProjectSaveDTO -> {
                    EmployeeProject employeeProject = new EmployeeProject();
                    Employee employee = employeeRepository.findById(employeeForProjectSaveDTO.getEmployeeId())
                            .orElseThrow(() -> new IllegalStateException("Employee not found with ID: " + employeeForProjectSaveDTO.getEmployeeId()));

                    employeeProject.setEmployee(employee);
                    employeeProject.setProject(project);
                    employeeProject.setRole(employeeForProjectSaveDTO.getRole());
                    employeeProject.setId(new EmployeeProjectId(employee.getId(), project.getId()));
                    return employeeProject;
                })
                .toList();

        employeeProjectRepository.saveAll(employeeProjects);
    }
}
