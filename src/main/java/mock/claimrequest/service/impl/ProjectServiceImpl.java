package mock.claimrequest.service.impl;

import mock.claimrequest.dto.employeeProject.EmployeeProjectDTO;
import mock.claimrequest.dto.project.ProjectDTO;
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
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

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

    @Override
    public List<ProjectDTO> list(){
        return projectRepository.findAll().stream().map((project)-> modelMapper.map(project, ProjectDTO.class))
                .toList();
    }

    @Override
    public List<ProjectDTO> listByInProgressStatus(ProjectStatus projectStatus){
        return projectRepository.findAllByProjectStatus(projectStatus).stream().map((project)-> modelMapper.map(project, ProjectDTO.class))
                .toList();
    }

    @Override
    public ProjectDTO getById(UUID projectId) {
        Project project = projectRepository.findById(projectId).orElseThrow(()-> new IllegalStateException("Project is not existed!"));
        return modelMapper.map(project, ProjectDTO.class);
    }

    @Override
    public List<EmployeeProjectDTO> getEmployeeProjectsByProjectId(UUID projectId) {
        List<EmployeeProject> employeeProjects= employeeProjectRepository.findByProjectId(projectId);
        return employeeProjects.stream().map(employeeProject -> {
            EmployeeProjectDTO dto = new EmployeeProjectDTO();
            dto.setEmployeeId(employeeProject.getEmployee().getId());
            dto.setAccountName(employeeProject.getEmployee().getAccount().getUserName());
            dto.setRole(employeeProject.getRole());
            return dto;
        }).toList();
    }

    @Override
    public void update(ProjectDTO projectDTO) {
        Project project = projectRepository.findById(projectDTO.getId()).orElseThrow(()->
                new IllegalStateException("Project not existed!"));

        List<EmployeeProject> employeeProjects = projectDTO.getEmployeeProjects().stream()
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

    @Override
    public void delete(UUID id) {
        projectRepository.findById(id).orElseThrow(()->
                new IllegalStateException("Project not existed!"));
        projectRepository.deleteById(id);
    }

}
