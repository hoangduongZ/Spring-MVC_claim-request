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

        List<EmployeeProject> employeeProjects = new ArrayList<>();
        if (employeeProjectDTOS != null) {
            for (EmployeeProjectDTO employeeProjectDTO : employeeProjectDTOS) {
                Employee employee = employeeRepository.findById(employeeProjectDTO.getEmployeeId())
                        .orElseThrow(() -> new IllegalStateException("Employee not found!"));

                EmployeeProject employeeProject = new EmployeeProject();
                EmployeeProjectId employeeProjectId = new EmployeeProjectId(employee.getId(), project.getId());
                employeeProject.setId(employeeProjectId);
                employeeProject.setEmployee(employee);
                employeeProject.setProject(project);
                employeeProject.setRole(employeeProjectDTO.getRole());
                employeeProject.setEmpProjectStatus(EmpProjectStatus.IN);
                employeeProject.setStartDate(employeeProjectDTO.getStartDate());
                employeeProject.setEndDate(employeeProjectDTO.getEndDate());
                employeeProjects.add(employeeProject);
            }
        }

        if (!employeeProjects.isEmpty()) {
            employeeProjectRepository.saveAll(employeeProjects);
        }
    }

    @Override
    public void update(ProjectDTO projectDTO) {
        Project project = projectRepository.findById(projectDTO.getId())
                .orElseThrow(() -> new IllegalStateException("Project not existed!"));

        project.setName(projectDTO.getName());
        project.setDescription(projectDTO.getDescription());
        project.setStartDate(projectDTO.getStartDate());
        project.setEndDate(projectDTO.getEndDate());
        project.setBudget(projectDTO.getBudget());
        project.setProjectStatus(projectDTO.getStatus());

        projectRepository.save(project);

        UUID projectId = project.getId();
        if(projectDTO.getEmployeeProjects()!= null){
            List<EmployeeProject> employeeProjectsRecieve = projectDTO.getEmployeeProjects().stream()
                    .map(employeeProjectDTO -> {
                        EmployeeProject employeeProject = new EmployeeProject();
                        EmployeeProjectId employeeProjectId = new EmployeeProjectId(employeeProjectDTO.getEmployeeId(), projectId);
                        Employee employee = employeeRepository.findById(employeeProjectDTO.getEmployeeId())
                                .orElseThrow(() -> new IllegalStateException("Employee not found!"));

                        employeeProject.setId(employeeProjectId);
                        employeeProject.setEmployee(employee);
                        employeeProject.setProject(project);
                        employeeProject.setRole(employeeProjectDTO.getRole());
                        employeeProject.setEmpProjectStatus(EmpProjectStatus.IN);
                        employeeProject.setStartDate(employeeProjectDTO.getStartDate());
                        employeeProject.setEndDate(employeeProjectDTO.getEndDate());
                        return employeeProject;
                    })
                    .toList();

            List<EmployeeProject> employeeProjectsInDB = employeeProjectRepository.findByProjectIdAndEmpProjectStatus(
                    projectId, EmpProjectStatus.IN);

            handleRemove(employeeProjectsInDB, employeeProjectsRecieve);
            handleSave(employeeProjectsInDB, employeeProjectsRecieve);
        }
    }

    private void handleRemove(List<EmployeeProject> employeeProjectsInDB, List<EmployeeProject> employeeProjectsRecieve) {
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
    }

    private void handleSave(List<EmployeeProject> employeeProjectsInDB, List<EmployeeProject> employeeProjectsRecieve) {
        List<EmployeeProject> toSave = new ArrayList<>();
        LocalDate now = LocalDate.now();
        LocalDate projectStartDate = employeeProjectsInDB.isEmpty() ? null : employeeProjectsInDB.get(0).getProject().getStartDate();
        LocalDate projectEndDate = employeeProjectsInDB.isEmpty() ? null : employeeProjectsInDB.get(0).getProject().getEndDate();

        for (EmployeeProject empProjectRecieve : employeeProjectsRecieve) {
            EmployeeProjectId empProjectId = empProjectRecieve.getId();

            Optional<EmployeeProject> empProjectInDBOptional = employeeProjectsInDB.stream()
                    .filter(empProjectInDB -> empProjectInDB.getId().equals(empProjectId))
                    .findFirst();

            if (empProjectInDBOptional.isPresent()) {
                EmployeeProject empProjectInDB = empProjectInDBOptional.get();
                boolean isUpdated = updateEmployeeProject(empProjectRecieve, empProjectInDB, projectStartDate, projectEndDate, now);
                if (isUpdated) {
                    toSave.add(empProjectInDB);
                }
            } else {
                validateProjectDates(empProjectRecieve, projectStartDate, projectEndDate);
                toSave.add(empProjectRecieve);
            }
        }

        if (!toSave.isEmpty()) {
            employeeProjectRepository.saveAll(toSave);
        }
    }

    private boolean updateEmployeeProject(EmployeeProject empProjectRecieve, EmployeeProject empProjectInDB,
                                          LocalDate projectStartDate, LocalDate projectEndDate, LocalDate now) {
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
            validateProjectDates(empProjectRecieve, projectStartDate, projectEndDate);

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
        }

        return isUpdated;
    }

    private void validateProjectDates(EmployeeProject empProjectRecieve, LocalDate projectStartDate, LocalDate projectEndDate) {
        if ((empProjectRecieve.getStartDate() != null &&
                (empProjectRecieve.getStartDate().isBefore(projectStartDate) ||
                        empProjectRecieve.getStartDate().isAfter(projectEndDate))) ||
                (empProjectRecieve.getEndDate() != null &&
                        (empProjectRecieve.getEndDate().isBefore(projectStartDate) ||
                                empProjectRecieve.getEndDate().isAfter(projectEndDate)))) {
            throw new IllegalStateException("Start date or end date is out of project range!");
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

    @Override
    public ProjectGetDTO getCurrentProject(Employee employee) {
        EmployeeProject employeeProject= employeeProjectRepository.findByEmployeeIdAndEmpProjectStatus(employee.getId(), EmpProjectStatus.IN);
        Project project= projectRepository.findById(employeeProject.getProject().getId()).get();
        return modelMapper.map(project, ProjectGetDTO.class);
    }

}
