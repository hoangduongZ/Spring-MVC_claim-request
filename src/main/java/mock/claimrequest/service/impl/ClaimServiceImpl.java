package mock.claimrequest.service.impl;

import mock.claimrequest.dto.claim.ClaimDetailDTO;
import mock.claimrequest.dto.claim.ClaimGetDTO;
import mock.claimrequest.dto.claim.ClaimSaveDTO;
import mock.claimrequest.entity.Claim;
import mock.claimrequest.entity.ClaimDetail;
import mock.claimrequest.entity.Employee;
import mock.claimrequest.entity.EmployeeProject;
import mock.claimrequest.entity.EmployeeProjectId;
import mock.claimrequest.entity.Project;
import mock.claimrequest.entity.entityEnum.AccountRole;
import mock.claimrequest.entity.entityEnum.ClaimStatus;
import mock.claimrequest.entity.entityEnum.EmpProjectStatus;
import mock.claimrequest.entity.entityEnum.ProjectRole;
import mock.claimrequest.repository.ClaimDetailRepository;
import mock.claimrequest.repository.ClaimRepository;
import mock.claimrequest.repository.EmployeeProjectRepository;
import mock.claimrequest.repository.EmployeeRepository;
import mock.claimrequest.repository.ProjectRepository;
import mock.claimrequest.security.AuthService;
import mock.claimrequest.service.ClaimService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class ClaimServiceImpl implements ClaimService {
    private final ClaimRepository claimRepository;
    private final EmployeeRepository employeeRepository;
    private final EmployeeProjectRepository employeeProjectRepository;
    private final AuthService authService;
    private final ProjectRepository projectRepository;
    private final ClaimDetailRepository claimDetailRepository;
    private final ModelMapper modelMapper;

    public ClaimServiceImpl(ClaimRepository claimRepository, EmployeeRepository employeeRepository, EmployeeProjectRepository employeeProjectRepository, AuthService authService, ProjectRepository projectRepository, ClaimDetailRepository claimDetailRepository, ModelMapper modelMapper) {
        this.claimRepository = claimRepository;
        this.employeeRepository = employeeRepository;
        this.employeeProjectRepository = employeeProjectRepository;
        this.authService = authService;
        this.projectRepository = projectRepository;
        this.claimDetailRepository = claimDetailRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public List<ClaimGetDTO> getClaimByStatus(ClaimStatus claimStatus) {
        AccountRole currentRole = authService.getCurrentRoleAccount();

        if (!Objects.equals(currentRole,AccountRole.ADMIN)) {
            if (currentRole == AccountRole.FINANCE) {
                return claimRepository.findAllByStatus(ClaimStatus.APPROVE).stream()
                        .map(this::convertToDTO)
                        .toList();
            }

            UUID employeeId = authService.getCurrentAccount().getEmployee().getId();
            EmployeeProject employeeProject = employeeProjectRepository.findByEmployeeIdAndEmpProjectStatus(employeeId, EmpProjectStatus.IN);
            if(employeeProject == null){
                return null;
            }

            return getEmployeeRoleInProject()
                    .map(role -> {
                        if (role == ProjectRole.PM) {
                            return getClaimsByStatusAndProject(claimStatus, employeeProject.getProject().getId());
                        } else {
                            return getClaimsByStatusAndEmployee(claimStatus, employeeId);
                        }
                    })
                    .orElseGet(() -> getClaimsByStatusAndEmployee(claimStatus, employeeId));
        }

        return claimRepository.findAllByStatus(claimStatus).stream().map(this::convertToDTO).toList();
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
        claimGetDto.setClaimDetailDTOList(
                claim.getClaimDetails().stream()
                        .map(claimDetail -> modelMapper.map(claimDetail, ClaimDetailDTO.class))
                        .toList()
        );
        claimGetDto.setDuration(claim.getDuration());
        return claimGetDto;
    }

    @Override
    public ClaimGetDTO findById(UUID id) {
        Claim claim= claimRepository.findById(id).orElseThrow(()-> new RuntimeException("Claim not exist"));
        return convertToDTO(claim);
    }

    @Override
    public void actionCreate(ClaimStatus claimStatus, ClaimSaveDTO claimSaveDTO) {
        Employee employee = employeeRepository.findByAccount(authService.getCurrentAccount());
        Project project = projectRepository.findById(claimSaveDTO.getProjectGetDTO().getId()).orElseThrow(()-> new IllegalStateException("Project not existed"));
        EmployeeProject employeeProject = employeeProjectRepository.findById(new EmployeeProjectId(employee.getId(), project.getId())).orElseThrow(
                ()->new IllegalStateException("EmployeeProject not existed")
        );
        Claim claim= new Claim();
        if (claimSaveDTO.getDuration().isEmpty()){
            claimSaveDTO.setDuration("0");
        }
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

    @Override
    public void updateStatus(ClaimStatus claimStatus, UUID id) {
        Claim claim = claimRepository.findById(id).orElseThrow(
                () -> new IllegalStateException("Claim is not existed!"));
        claim.setStatus(claimStatus);
        claimRepository.save(claim);
    }

    @Override
    public void update(ClaimGetDTO claimGetDTO, UUID id, String status) {
        Claim claim = claimRepository.findById(id)
                .orElseThrow(() -> new IllegalStateException("Claim is not existed!"));

        claim.setTitle(claimGetDTO.getTitle());
        claim.setRequestReason(claimGetDTO.getRequestReason());
        claim.setAmount(claimGetDTO.getAmount());
        claim.setDuration(claimGetDTO.getDuration());

        claim.setStatus(ClaimStatus.valueOf(status.toUpperCase()));

        Project project = projectRepository.findByName(claimGetDTO.getProjectName())
                .orElseThrow(() -> new IllegalStateException("Project not found!"));
        claim.setProject(project);

        claim.setUpdatedTime(LocalDateTime.now());

        List<ClaimDetail> claimDetails = claimGetDTO.getClaimDetailDTOList().stream()
                .map(detailDTO -> {
                    ClaimDetail claimDetail = new ClaimDetail();
                    claimDetail.setStartTime(detailDTO.getStartTime());
                    claimDetail.setEndTime(detailDTO.getEndTime());
                    claimDetail.setClaim(claim);
                    return claimDetail;
                }).toList();

        claim.getClaimDetails().clear();
        claim.getClaimDetails().addAll(claimDetails);

        claimRepository.save(claim);
    }

    
    

}
