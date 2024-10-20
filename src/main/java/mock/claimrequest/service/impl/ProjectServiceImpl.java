package mock.claimrequest.service.impl;

import mock.claimrequest.dto.employeeProject.EmployeeProjectDTO;
import mock.claimrequest.dto.project.ProjectDTO;
import mock.claimrequest.dto.project.ProjectGetDTO;
import mock.claimrequest.dto.project.ProjectSaveDTO;
import mock.claimrequest.entity.Employee;
import mock.claimrequest.entity.EmployeeProject;
import mock.claimrequest.entity.EmployeeProjectId;
import mock.claimrequest.entity.Project;
import mock.claimrequest.entity.entityEnum.EmpProjectStatus;
import mock.claimrequest.entity.entityEnum.EmployeeStatus;
import mock.claimrequest.entity.entityEnum.ProjectStatus;
import mock.claimrequest.repository.EmployeeProjectRepository;
import mock.claimrequest.repository.EmployeeRepository;
import mock.claimrequest.repository.ProjectRepository;
import mock.claimrequest.security.AuthService;
import mock.claimrequest.service.ProjectService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@Transactional
@Service
public class ProjectServiceImpl implements ProjectService {
    private final ProjectRepository projectRepository;
    private final EmployeeProjectRepository employeeProjectRepository;
    private final EmployeeRepository employeeRepository;
    private final ModelMapper modelMapper;
    private final AuthService authService;

    public ProjectServiceImpl(ProjectRepository projectRepository, EmployeeProjectRepository employeeProjectRepository, EmployeeRepository employeeRepository, ModelMapper modelMapper, AuthService authService) {
        this.projectRepository = projectRepository;
        this.employeeProjectRepository = employeeProjectRepository;
        this.employeeRepository = employeeRepository;
        this.modelMapper = modelMapper;
        this.authService = authService;
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
        List<EmployeeProject> employeeProjects = employeeProjectRepository.findByProjectIdAndEmpProjectStatus(projectId, EmpProjectStatus.IN);
        return employeeProjects.stream().map(employeeProject -> {
            EmployeeProjectDTO dto = new EmployeeProjectDTO();
            dto.setEmployeeId(employeeProject.getEmployee().getId());
            dto.setAccountName(employeeProject.getEmployee().getAccount().getUserName());
            dto.setRole(employeeProject.getRole());
            dto.setStartDate(employeeProject.getStartDate());
            dto.setEndDate(employeeProject.getEndDate());
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

        if (projectSaveDTO.getStartDate().isEqual(LocalDate.now())) {
            project.setProjectStatus(ProjectStatus.IN_PROGRESS);
        } else {
            project.setProjectStatus(ProjectStatus.NOT_STARTED);
        }

        projectRepository.save(project);

        List<EmployeeProjectDTO> employeeProjectDTOS = projectSaveDTO.getEmployeeProjects();

        List<EmployeeProject> employeeProjects;
        if (employeeProjectDTOS == null) {
            employeeProjects = new ArrayList<>();
            employeeProjectRepository.saveAll(employeeProjects);
        } else {
            List<EmployeeProject> existingEmployeeProjects = employeeProjectRepository.findByProjectId(project.getId());

        }

    }

    @Override
    public void update(ProjectDTO projectDTO) {
        Project project = projectRepository.findById(projectDTO.getId())
                .orElseThrow(() -> new IllegalStateException("Project not existed!"));

        UUID projectId = project.getId();

        List<EmployeeProject> employeeProjectsRecieve = projectDTO.getEmployeeProjects().stream()
                .map(employeeProjectDTO -> {
                    EmployeeProject employeeProject = new EmployeeProject();
                    EmployeeProjectId employeeProjectId = new EmployeeProjectId(employeeProjectDTO.getEmployeeId(), projectId);
                    Employee employee = employeeRepository.findById(employeeProjectDTO.getEmployeeId())
                            .orElseThrow(() -> new IllegalStateException("Employee not found!"));

                    Project projectZ = projectRepository.findById(projectId)
                            .orElseThrow(() -> new IllegalStateException("Project not found!"));
                    employeeProject.setId(employeeProjectId);
                    employeeProject.setEmployee(employee);
                    employeeProject.setProject(projectZ);
                    employeeProject.setRole(employeeProjectDTO.getRole());
                    employeeProject.setEmpProjectStatus(EmpProjectStatus.IN);
                    employeeProject.setStartDate(employeeProjectDTO.getStartDate());
                    employeeProject.setEndDate(employeeProjectDTO.getEndDate());
                    return employeeProject;
                })
                .toList();

        List<EmployeeProject> employeeProjectsInDB = employeeProjectRepository.findByProjectIdAndEmpProjectStatus(
                projectId, EmpProjectStatus.IN);

        LocalDate projectStartDate = project.getStartDate();
        LocalDate projectEndDate = project.getEndDate();
        LocalDate now = LocalDate.now();

        List<EmployeeProject> toDelete = employeeProjectsInDB.stream()
                .filter(empProjectInDB -> employeeProjectsRecieve.stream()
                        .noneMatch(empProjectRecieve -> empProjectRecieve.getId().equals(empProjectInDB.getId())))
                .toList();

        toDelete.forEach(employeeProject -> {
            employeeProject.getEmployee().setEmployeeStatus(EmployeeStatus.FREE);
        });

        if (!toDelete.isEmpty()) {
            employeeProjectRepository.deleteAll(toDelete);
        }

        List<EmployeeProject> toSave = new ArrayList<>();

        for (EmployeeProject empProjectRecieve : employeeProjectsRecieve) {
            EmployeeProjectId empProjectId = empProjectRecieve.getId();

            Optional<EmployeeProject> empProjectInDBOptional = employeeProjectsInDB.stream()
                    .filter(empProjectInDB -> empProjectInDB.getId().equals(empProjectId))
                    .findFirst();

            if (empProjectInDBOptional.isPresent()) {
                EmployeeProject empProjectInDB = empProjectInDBOptional.get();

                boolean isUpdated = false;

                if (!empProjectRecieve.getRole().equals(empProjectInDB.getRole())) {
                    empProjectInDB.setRole(empProjectRecieve.getRole());
                    isUpdated = true;
                }

                if (empProjectRecieve.getStartDate() != null &&
                        !Objects.equals(empProjectRecieve.getStartDate(), empProjectInDB.getStartDate())) {
                    empProjectInDB.setStartDate(empProjectRecieve.getStartDate());
                    isUpdated = true;
                }

                if (empProjectRecieve.getEndDate() != null &&
                        !Objects.equals(empProjectRecieve.getEndDate(), empProjectInDB.getEndDate())) {
                    empProjectInDB.setEndDate(empProjectRecieve.getEndDate());
                    isUpdated = true;
                }

                if (isUpdated) {
                    if ((empProjectRecieve.getStartDate() != null &&
                            (empProjectRecieve.getStartDate().isBefore(projectStartDate) ||
                                    empProjectRecieve.getStartDate().isAfter(projectEndDate))) ||
                            (empProjectRecieve.getEndDate() != null &&
                                    (empProjectRecieve.getEndDate().isBefore(projectStartDate) ||
                                            empProjectRecieve.getEndDate().isAfter(projectEndDate)))) {
                        throw new IllegalStateException("Start date or end date is out of project range!");
                    }

                    if (empProjectRecieve.getStartDate() != null &&
                            (empProjectRecieve.getStartDate().isEqual(now) || empProjectRecieve.getStartDate().isAfter(now))) {
                        empProjectInDB.getEmployee().setEmployeeStatus(EmployeeStatus.WORKING);
                        empProjectInDB.setEmpProjectStatus(EmpProjectStatus.IN);
                    }

                    if (empProjectRecieve.getEndDate() != null &&
                            empProjectRecieve.getEndDate().isEqual(now)) {
                        empProjectInDB.getEmployee().setEmployeeStatus(EmployeeStatus.FREE);
                        empProjectInDB.setEmpProjectStatus(EmpProjectStatus.OUT);
                    }

                    toSave.add(empProjectInDB);
                }

            } else {
                if ((empProjectRecieve.getStartDate() != null &&
                        (empProjectRecieve.getStartDate().isBefore(projectStartDate) ||
                                empProjectRecieve.getStartDate().isAfter(projectEndDate))) ||
                        (empProjectRecieve.getEndDate() != null &&
                                (empProjectRecieve.getEndDate().isBefore(projectStartDate) ||
                                        empProjectRecieve.getEndDate().isAfter(projectEndDate)))) {
                    throw new IllegalStateException("Start date or end date is out of project range!");
                }

                toSave.add(empProjectRecieve);
            }
        }

        if (!toSave.isEmpty()) {
            employeeProjectRepository.saveAll(toSave);
        }
    }

    @Override
    public void delete(UUID id) {
        projectRepository.findById(id).orElseThrow(() ->
                new IllegalStateException("Project not existed!"));
        projectRepository.deleteById(id);
    }

    @Override
    public List<ProjectGetDTO> getProjectForClaim(UUID id) {
        Employee employee = employeeRepository.findByAccount(authService.getCurrentAccount());

        if (id == null) {
            throw new IllegalArgumentException("Id not null");
        }
        return projectRepository.findActiveProjectsByEmployeeId(employee.getId()).stream().map(project -> {
            var projectDto = new ProjectGetDTO();
            projectDto.setId(project.getId());
            projectDto.setName(project.getName());
            return projectDto;
        }).toList();
    }

}
