package mock.claimrequest.service.impl;

import java.time.Duration;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import mock.claimrequest.dto.claim.ClaimGetDto;
import mock.claimrequest.dto.test.ClaimTestDTO;
import mock.claimrequest.entity.Claim;
import mock.claimrequest.entity.ClaimDetail;
import mock.claimrequest.entity.Employee;
import mock.claimrequest.entity.Project;
import mock.claimrequest.entity.entityEnum.ClaimStatus;
import mock.claimrequest.repository.ClaimDetailRepository;
import mock.claimrequest.repository.ClaimRepository;
import mock.claimrequest.repository.EmployeeRepository;
import mock.claimrequest.repository.ProjectRepository;
import mock.claimrequest.service.ClaimService;

@Service
public class ClaimServiceImpl implements ClaimService {
    private final ClaimRepository claimRepository;
    private final EmployeeRepository employeeRepository;

    private final ProjectRepository projectRepository ;

    private final ClaimDetailRepository claimDetailRepository ;

    public ClaimServiceImpl(ClaimRepository claimRepository, EmployeeRepository employeeRepository, ProjectRepository projectRepository, ClaimDetailRepository claimDetailRepository) {
        this.claimRepository = claimRepository;
        this.employeeRepository = employeeRepository;
        this.projectRepository = projectRepository;
        this.claimDetailRepository = claimDetailRepository;
    }

    @Override
    public List<ClaimGetDto> getClaimByStatus(ClaimStatus claimStatus){
        return claimRepository.findAllByStatus(claimStatus).stream().map(
                claim -> {
                    ClaimGetDto claimGetDto= new ClaimGetDto();
                    claimGetDto.setEmployeeName(claim.getEmployee().getFirstname() + " " +claim.getEmployee().getLastname());
                    claimGetDto.setRequestReason(claim.getRequestReason());
                    claimGetDto.setProjectName(claim.getProject().getName());
                    claimGetDto.setCreatedTime(claim.getCreatedTime());
                    claimGetDto.setAmount(claim.getAmount());
                    claimGetDto.setStatus(claim.getStatus());
                    claimGetDto.setId(claim.getId());
                    return claimGetDto;
                }
        ).toList();
    }

    public void paidClaim(UUID id){
        Claim claim = claimRepository.findById(id).orElseThrow(()-> new RuntimeException("Claim not exist"));
        claim.setStatus(ClaimStatus.PAID);
        claimRepository.save(claim);
    }

    @Override
    public void cancelClaim(UUID id) {
        Claim claim = claimRepository.findById(id).orElseThrow(()-> new RuntimeException("Claim not exist"));
        claim.setStatus(ClaimStatus.CANCEL);
        claimRepository.save(claim);
    }

    @Override
    public void submitClaim(ClaimTestDTO claimTestDTO) {
        if (claimTestDTO == null){
            throw new IllegalStateException("claim is not null !");
        }
        else{

            Employee employee = employeeRepository.findById(claimTestDTO.getEmployeeId())
                    .orElseThrow(()-> new RuntimeException("Employee not exist"));

            Project project = projectRepository.findById(claimTestDTO.getProjectId())
                    .orElseThrow(()-> new RuntimeException("Project not exist"));

            Claim claim = new Claim() ;

            claim.setRequestReason(claimTestDTO.getRequestReason());
            claim.setStatus(ClaimStatus.PENDING);

            // if status processing, thêm vào date
            claim.setCreatedTime(claimTestDTO.getDate());
            claim.setTitle(claimTestDTO.getTitle());
            claim.setProject(project); //lấy từ name trong form th:value : id , th:text : name , option
            claim.setEmployee(employee); //security :tính sau
            Duration duration = Duration.between(claimTestDTO.getStartTime(), claimTestDTO.getEndTime());
            claim.setDuration(duration.toMinutes());

            claimRepository.save(claim);

            ClaimDetail claimDetail = new ClaimDetail();
            claimDetail.setStartTime(claimTestDTO.getStartTime());
            claimDetail.setEndTime(claimTestDTO.getEndTime());
            claimDetail.setClaim(claim);

            claimDetailRepository.save(claimDetail);









        }



    }

    @Override
    public ClaimGetDto findById(UUID id) {
        Claim claim= claimRepository.findById(id).orElseThrow(()-> new RuntimeException("Claim not exist"));
        ClaimGetDto claimGetDto = new ClaimGetDto();
        claimGetDto.setEmployeeName(claim.getEmployee().getFirstname() + " " + claim.getEmployee().getLastname());
        claimGetDto.setRequestReason(claim.getRequestReason());
        claimGetDto.setProjectName(claim.getProject().getName());
        claimGetDto.setCreatedTime(claim.getCreatedTime());
        claimGetDto.setAmount(claim.getAmount());
        claimGetDto.setStatus(claim.getStatus());
        claimGetDto.setId(claim.getId());
        return claimGetDto;
    }


}
