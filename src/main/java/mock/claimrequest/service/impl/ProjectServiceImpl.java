package mock.claimrequest.service.impl;

import mock.claimrequest.dto.claim.ClaimSaveDTO;
import mock.claimrequest.dto.employeeProject.EmployeeProjectDTO;
import mock.claimrequest.dto.project.ProjectDTO;
import mock.claimrequest.dto.project.ProjectGetDTO;
import mock.claimrequest.dto.project.ProjectSaveDTO;
import mock.claimrequest.entity.Account;
import mock.claimrequest.entity.Claim;
import mock.claimrequest.entity.ClaimDetail;
import mock.claimrequest.entity.Employee;
import mock.claimrequest.entity.EmployeeProject;
import mock.claimrequest.entity.EmployeeProjectId;
import mock.claimrequest.entity.Project;
import mock.claimrequest.entity.entityEnum.ClaimStatus;
import mock.claimrequest.entity.entityEnum.EmpProjectStatus;
import mock.claimrequest.entity.entityEnum.EmployeeStatus;
import mock.claimrequest.entity.entityEnum.ProjectStatus;
import mock.claimrequest.repository.AccountRepository;
import mock.claimrequest.repository.ClaimDetailRepository;
import mock.claimrequest.repository.ClaimRepository;
import mock.claimrequest.repository.EmployeeProjectRepository;
import mock.claimrequest.repository.EmployeeRepository;
import mock.claimrequest.repository.ProjectRepository;
import mock.claimrequest.service.ProjectService;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
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
    private final AccountRepository accountRepository;
    private final ClaimRepository claimRepository;
    private final ClaimDetailRepository claimDetailRepository;

    public ProjectServiceImpl(ProjectRepository projectRepository, EmployeeProjectRepository employeeProjectRepository, EmployeeRepository employeeRepository, ModelMapper modelMapper, AccountRepository accountRepository, ClaimRepository claimRepository, ClaimDetailRepository claimDetailRepository) {
        this.projectRepository = projectRepository;
        this.employeeProjectRepository = employeeProjectRepository;
        this.employeeRepository = employeeRepository;
        this.modelMapper = modelMapper;
        this.accountRepository = accountRepository;
        this.claimRepository = claimRepository;
        this.claimDetailRepository = claimDetailRepository;
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

        List<EmployeeProjectDTO> employeeProjectDTOS= projectSaveDTO.getEmployeeProjects();

        List<EmployeeProject> employeeProjects;
        if (employeeProjectDTOS == null) {
            employeeProjects = new ArrayList<>();
        }else{
            employeeProjects = mapEmployeeProjects(project, employeeProjectDTOS);
        }
        employeeProjectRepository.saveAll(employeeProjects);
    }

    @Override
    public void update(ProjectDTO projectDTO) {
        Project project = projectRepository.findById(projectDTO.getId())
                .orElseThrow(() -> new IllegalStateException("Project not existed!"));

        if (projectDTO.getEmployeeProjects() == null || projectDTO.getEmployeeProjects().isEmpty()) {
            handleEmployeeStatusUpdate(project);
            return;
        }

        List<EmployeeProject> employeeProjects = mapEmployeeProjects(project, projectDTO.getEmployeeProjects());
        employeeProjectRepository.saveAll(employeeProjects);
    }

    private void handleEmployeeStatusUpdate(Project project) {
        List<EmployeeProject> existingEmployeeProjects = employeeProjectRepository.findByProjectIdAndEmpProjectStatus(project.getId(), EmpProjectStatus.IN);
        existingEmployeeProjects.forEach(empProject -> {
            empProject.getEmployee().setEmployeeStatus(EmployeeStatus.FREE);
            empProject.setEmpProjectStatus(EmpProjectStatus.OUT);
        });
        employeeProjectRepository.saveAll(existingEmployeeProjects);
    }

    private List<EmployeeProject> mapEmployeeProjects(Project project, List<EmployeeProjectDTO> employeeProjectDTOS) {
        List<EmployeeProject> existingEmployeeProjects = employeeProjectRepository.findByProjectId(project.getId());

        List<EmployeeProject> newEmployeeProjects = createNewEmployeeProjects(project, employeeProjectDTOS);
        updateExistingEmployeeProjects(existingEmployeeProjects, newEmployeeProjects);

        return newEmployeeProjects;
    }

    private List<EmployeeProject> createNewEmployeeProjects(Project project, List<EmployeeProjectDTO> employeeProjectDTOS) {
        return employeeProjectDTOS.stream()
                .map(dto -> {
                    if (dto.getEmployeeId() != null) {
                        Employee employee = employeeRepository.findById(dto.getEmployeeId())
                                .orElseThrow(() -> new IllegalStateException("Employee not found with ID: " + dto.getEmployeeId()));

                        employee.setEmployeeStatus(EmployeeStatus.WORKING);

                        EmployeeProject employeeProject = new EmployeeProject();
                        employeeProject.setEmployee(employee);
                        employeeProject.setProject(project);
                        employeeProject.setRole(dto.getRole());
                        employeeProject.setId(new EmployeeProjectId(employee.getId(), project.getId()));
                        employeeProject.setEmpProjectStatus(EmpProjectStatus.IN);

                        return employeeProject;
                    }
                    return null;
                })
                .filter(Objects::nonNull)
                .toList();
    }

    private void updateExistingEmployeeProjects(List<EmployeeProject> existingEmployeeProjects, List<EmployeeProject> newEmployeeProjects) {
        List<EmployeeProject> employeesToRemove = existingEmployeeProjects.stream()
                .filter(existing -> newEmployeeProjects.stream()
                        .noneMatch(newProject -> newProject.getEmployee().getId().equals(existing.getEmployee().getId())))
                .toList();

        for (var emp : employeesToRemove) {
            boolean isWorkingOnAnotherProject = employeeProjectRepository.existsByEmployeeIdAndEmpProjectStatus(emp.getEmployee().getId(), EmpProjectStatus.IN);

            if (!isWorkingOnAnotherProject) {
                emp.getEmployee().setEmployeeStatus(EmployeeStatus.FREE);
            }
            emp.setEmpProjectStatus(EmpProjectStatus.OUT);
        }

        if (!employeesToRemove.isEmpty()) {
            employeeProjectRepository.saveAll(employeesToRemove);
        }

        for (var newProject : newEmployeeProjects) {
            EmployeeProject existingProject = existingEmployeeProjects.stream()
                    .filter(existing -> existing.getEmployee().getId().equals(newProject.getEmployee().getId()))
                    .findFirst()
                    .orElse(null);

            if (existingProject != null) {
                existingProject.setEmpProjectStatus(EmpProjectStatus.IN);
            }
        }

        employeeProjectRepository.saveAll(newEmployeeProjects);
    }

    @Override
    public void delete(UUID id) {
        projectRepository.findById(id).orElseThrow(() ->
                new IllegalStateException("Project not existed!"));
        projectRepository.deleteById(id);
    }

    @Override
    public List<ProjectGetDTO> getProjectForClaim(UUID id){
        Employee employee = employeeRepository.findByAccount(getCurrentAccount());

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
    public void actionByStatus(ClaimStatus claimStatus, ClaimSaveDTO claimSaveDTO) {
        Employee employee = employeeRepository.findByAccount(getCurrentAccount());
        Project project = projectRepository.findById(claimSaveDTO.getProjectGetDTO().getId()).orElseThrow(()-> new IllegalStateException("Project not existed"));
        EmployeeProject employeeProject = employeeProjectRepository.findById(new EmployeeProjectId(employee.getId(), project.getId())).orElseThrow(
                ()->new IllegalStateException("EmployeeProject not existed")
        );
        Claim claim= new Claim();
        claim.setProject(project);
        claim.setTitle(claimSaveDTO.getTitle());
        claim.setRequestReason(claimSaveDTO.getRequestReason());
        claim.setDuration(Double.parseDouble(claimSaveDTO.getDuration()));
        claim.setStatus(claimStatus);
        claim.setEmployee(employee);
        claim.setAmount(BigDecimal.valueOf(employeeProject.getRole().getSalary() * claim.getDuration()));
        claimRepository.save(claim);

        if (claimSaveDTO.getClaimDetails() != null && !claimSaveDTO.getClaimDetails().isEmpty()) {
            List<ClaimDetail> claimDetails = claimSaveDTO.getClaimDetails().stream()
                    .map(detailDTO -> {
                        ClaimDetail claimDetail = new ClaimDetail();
                        claimDetail.setClaim(claim);
                        claimDetail.setStartTime(detailDTO.getStartTime());
                        claimDetail.setEndTime(detailDTO.getEndTime());
                        return claimDetail;
                    })
                    .collect(Collectors.toList());

            claimDetailRepository.saveAll(claimDetails);
        }
    }

    private Account getCurrentAccount() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            String email= Objects.requireNonNull(userDetails.getUsername());
            return accountRepository.findByEmail(email);
        }
        return null;

    }


}
