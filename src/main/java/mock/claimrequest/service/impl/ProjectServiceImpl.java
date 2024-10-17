package mock.claimrequest.service.impl;

import mock.claimrequest.dto.employeeProject.EmployeeProjectDTO;
import mock.claimrequest.dto.project.ProjectDTO;
import mock.claimrequest.dto.project.ProjectSaveDTO;
import mock.claimrequest.entity.Employee;
import mock.claimrequest.entity.EmployeeProject;
import mock.claimrequest.entity.EmployeeProjectId;
import mock.claimrequest.entity.Project;
import mock.claimrequest.entity.entityEnum.EmployeeStatus;
import mock.claimrequest.entity.entityEnum.ProjectStatus;
import mock.claimrequest.repository.EmployeeProjectRepository;
import mock.claimrequest.repository.EmployeeRepository;
import mock.claimrequest.repository.ProjectRepository;
import mock.claimrequest.service.ProjectService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

@Transactional
@Service
public class ProjectServiceImpl implements ProjectService {
    private final ProjectRepository projectRepository;
    private final EmployeeProjectRepository employeeProjectRepository;
    private final EmployeeRepository employeeRepository;
    private final ModelMapper modelMapper;

    public ProjectServiceImpl(ProjectRepository projectRepository, EmployeeProjectRepository employeeProjectRepository, EmployeeRepository employeeRepository, ModelMapper modelMapper) {
        this.projectRepository = projectRepository;
        this.employeeProjectRepository = employeeProjectRepository;
        this.employeeRepository = employeeRepository;
        this.modelMapper = modelMapper;
    }


    @Override
    public List<ProjectDTO> list() {
        return projectRepository.findAll().stream().map((project) -> modelMapper.map(project, ProjectDTO.class))
                .toList();
    }

    @Override
    public List<ProjectDTO> listByInProgressStatus(ProjectStatus projectStatus) {
        return projectRepository.findAllByProjectStatus(projectStatus).stream().map((project) -> modelMapper.map(project, ProjectDTO.class))
                .toList();
    }

    @Override
    public ProjectDTO getById(UUID projectId) {
        Project project = projectRepository.findById(projectId).orElseThrow(() -> new IllegalStateException("Project is not existed!"));
        return modelMapper.map(project, ProjectDTO.class);
    }

    @Override
    public List<EmployeeProjectDTO> getEmployeeProjectsByProjectId(UUID projectId) {
        List<EmployeeProject> employeeProjects = employeeProjectRepository.findByProjectId(projectId);
        return employeeProjects.stream().map(employeeProject -> {
            EmployeeProjectDTO dto = new EmployeeProjectDTO();
            dto.setEmployeeId(employeeProject.getEmployee().getId());
            dto.setAccountName(employeeProject.getEmployee().getAccount().getUserName());
            dto.setRole(employeeProject.getRole());
            return dto;
        }).toList();
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

        List<EmployeeProject> employeeProjects = mapEmployeeProjects(project, projectSaveDTO.getEmployeeProjects());

        employeeProjectRepository.saveAll(employeeProjects);
    }

    @Override
    public void update(ProjectDTO projectDTO) {
        Project project = projectRepository.findById(projectDTO.getId()).orElseThrow(() ->
                new IllegalStateException("Project not existed!"));

        List<EmployeeProject> employeeProjects = mapEmployeeProjects(project, projectDTO.getEmployeeProjects());
        employeeProjectRepository.saveAll(employeeProjects);
    }

    private List<EmployeeProject> mapEmployeeProjects(Project project, List<EmployeeProjectDTO> employeeProjectDTOS) {
        if (employeeProjectDTOS == null) {
            return new ArrayList<>();
        }

        List<EmployeeProject> existingEmployeeProjects = employeeProjectRepository.findByProjectId(project.getId());

        List<EmployeeProject> newEmployeeProjects = employeeProjectDTOS.stream()
                .map(employeeForProjectSaveDTO -> {
                    if (employeeForProjectSaveDTO.getEmployeeId() != null) {
                        Employee employee = employeeRepository.findById(employeeForProjectSaveDTO.getEmployeeId())
                                .orElseThrow(() -> new IllegalStateException("Employee not found with ID: " + employeeForProjectSaveDTO.getEmployeeId()));

                        employee.setEmployeeStatus(EmployeeStatus.WORKING);

                        EmployeeProject employeeProject = new EmployeeProject();
                        employeeProject.setEmployee(employee);
                        employeeProject.setProject(project);
                        employeeProject.setRole(employeeForProjectSaveDTO.getRole());
                        employeeProject.setId(new EmployeeProjectId(employee.getId(), project.getId()));

                        return employeeProject;
                    }
                    return null;
                })
                .filter(Objects::nonNull)
                .toList();

        List<EmployeeProject> employeesToRemove = existingEmployeeProjects.stream()
                .filter(existing -> newEmployeeProjects.stream()
                        .noneMatch(newProject -> newProject.getEmployee().getId().equals(existing.getEmployee().getId())))
                .collect(Collectors.toList());

        if(!employeesToRemove.isEmpty()){
            employeeProjectRepository.deleteAll(employeesToRemove);
        }
        return newEmployeeProjects;
    }

    @Override
    public void delete(UUID id) {
        projectRepository.findById(id).orElseThrow(() ->
                new IllegalStateException("Project not existed!"));
        projectRepository.deleteById(id);
    }


}
