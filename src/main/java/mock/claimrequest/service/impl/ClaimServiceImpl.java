package mock.claimrequest.service.impl;

import mock.claimrequest.dto.claim.ClaimGetDTO;
import mock.claimrequest.entity.Claim;
import mock.claimrequest.entity.Employee;
import mock.claimrequest.entity.EmployeeProject;
import mock.claimrequest.entity.Project;
import mock.claimrequest.entity.entityEnum.ClaimStatus;
import mock.claimrequest.entity.entityEnum.EmpProjectStatus;
import mock.claimrequest.entity.entityEnum.ProjectRole;
import mock.claimrequest.repository.ClaimRepository;
import mock.claimrequest.repository.EmployeeProjectRepository;
import mock.claimrequest.repository.EmployeeRepository;
import mock.claimrequest.repository.ProjectRepository;
import mock.claimrequest.security.AuthService;
import mock.claimrequest.service.ClaimService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class ClaimServiceImpl implements ClaimService {
    private final ClaimRepository claimRepository;
    private final EmployeeRepository employeeRepository;
    private final EmployeeProjectRepository employeeProjectRepository;
    private final AuthService authService;
    private final ProjectRepository projectRepository;

    public ClaimServiceImpl(ClaimRepository claimRepository, EmployeeRepository employeeRepository, EmployeeProjectRepository employeeProjectRepository, AuthService authService, ProjectRepository projectRepository) {
        this.claimRepository = claimRepository;
        this.employeeRepository = employeeRepository;
        this.employeeProjectRepository = employeeProjectRepository;
        this.authService = authService;
        this.projectRepository = projectRepository;
    }

    @Override
    public List<ClaimGetDTO> getClaimByStatus(ClaimStatus claimStatus) {
        UUID employeeId = authService.getCurrentAccount().getEmployee().getId();

        EmployeeProject employeeProject = employeeProjectRepository.findByEmployeeIdAndEmpProjectStatus(employeeId, EmpProjectStatus.IN);

        Optional<ProjectRole> roleOpt = getEmployeeRoleInProject();

        if (roleOpt.isPresent() && roleOpt.get() == ProjectRole.PM) {
            return getClaimsByStatusAndProject(claimStatus, employeeProject.getProject().getId());
        }

        return getClaimsByStatusAndEmployee(claimStatus, employeeId);
    }

    @Override
    public Optional<ProjectRole> getEmployeeRoleInProject() {
        UUID employeeId = authService.getCurrentAccount().getEmployee().getId();
        EmployeeProject employeeProject = employeeProjectRepository.findByEmployeeIdAndEmpProjectStatus(employeeId, EmpProjectStatus.IN);
        return employeeProjectRepository.findByEmployeeIdAndProjectId(employeeId, employeeProject.getProject().getId())
                .map(EmployeeProject::getRole);
    }

    private List<ClaimGetDTO> getClaimsByStatusAndProject(ClaimStatus claimStatus, UUID projectId) {
        Project project= projectRepository.findById(projectId).orElseThrow(()
        -> new IllegalStateException("Project not existed"));
        return claimRepository.findAllByStatusAndProject(claimStatus, project).stream()
                .map(this::convertToDTO)
                .toList();
    }

    private List<ClaimGetDTO> getClaimsByStatusAndEmployee(ClaimStatus claimStatus, UUID employeeId) {
        Employee employee = employeeRepository.findById(employeeId).orElseThrow(
                () -> new IllegalStateException("Employee not found!"));
        return claimRepository.findAllByStatusAndEmployee(claimStatus, employee).stream()
                .map(this::convertToDTO)
                .toList();
    }

    private ClaimGetDTO convertToDTO(Claim claim) {
        ClaimGetDTO claimGetDto = new ClaimGetDTO();
        claimGetDto.setEmployeeName(claim.getEmployee().getFirstname() + " " + claim.getEmployee().getLastname());
        claimGetDto.setRequestReason(claim.getRequestReason());
        claimGetDto.setProjectName(claim.getProject().getName());
        claimGetDto.setCreatedTime(claim.getCreatedTime());
        claimGetDto.setUpdatedTime(claim.getUpdatedTime());
        claimGetDto.setAmount(claim.getAmount());
        claimGetDto.setStatus(claim.getStatus());
        claimGetDto.setId(claim.getId());
        claimGetDto.setTitle(claim.getTitle());
        return claimGetDto;
    }

    @Override
    public ClaimGetDTO findById(UUID id) {
        Claim claim= claimRepository.findById(id).orElseThrow(()-> new RuntimeException("Claim not exist"));
        return convertToDTO(claim);
    }

}
