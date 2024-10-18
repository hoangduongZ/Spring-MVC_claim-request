package mock.claimrequest.service.impl;

import java.time.Duration;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import mock.claimrequest.dto.test.ClaimDetailTestDTO;
import mock.claimrequest.entity.Employee;
import mock.claimrequest.entity.Project;
import org.springframework.stereotype.Service;

import mock.claimrequest.dto.claim.ClaimGetDTO;
import mock.claimrequest.dto.test.ClaimTestDTO;
import mock.claimrequest.entity.Claim;
import mock.claimrequest.entity.ClaimDetail;
import mock.claimrequest.entity.entityEnum.ClaimStatus;
import mock.claimrequest.repository.ClaimDetailRepository;
import mock.claimrequest.repository.ClaimRepository;
import mock.claimrequest.repository.EmployeeRepository;
import mock.claimrequest.repository.ProjectRepository;
import mock.claimrequest.service.ClaimService;

import static org.springframework.data.jpa.domain.AbstractPersistable_.id;

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
    public List<ClaimGetDTO> getClaimByStatus(ClaimStatus claimStatus){
        return claimRepository.findAllByStatus(claimStatus).stream().map(
                claim -> {
                    ClaimGetDTO claimGetDto= new ClaimGetDTO();
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
    public ClaimGetDTO findById(UUID id) {
        Claim claim= claimRepository.findById(id).orElseThrow(()-> new RuntimeException("Claim not exist"));
        ClaimGetDTO claimGetDto = new ClaimGetDTO();
        claimGetDto.setEmployeeName(claim.getEmployee().getFirstname() + " " + claim.getEmployee().getLastname());
        claimGetDto.setRequestReason(claim.getRequestReason());
        claimGetDto.setProjectName(claim.getProject().getName());
        claimGetDto.setCreatedTime(claim.getCreatedTime());
        claimGetDto.setAmount(claim.getAmount());
        claimGetDto.setStatus(claim.getStatus());
        claimGetDto.setId(claim.getId());
        return claimGetDto;
    }

    @Override
    public void cancelClaim(UUID id) {
        Claim claim = claimRepository.findById(id).orElseThrow(()-> new RuntimeException("Claim not exist"));
        claim.setStatus(ClaimStatus.CANCEL);
        claimRepository.save(claim);
    }

    @Override
    public void submitClaimById(UUID id) {

        Claim claim = claimRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Claim not exist"));

        // Cập nhật trạng thái thành APPROVE
        claim.setStatus(ClaimStatus.APPROVE);

        // Lưu lại Claim đã được cập nhật
        claimRepository.save(claim);
    }


    @Override
    public List<ClaimTestDTO> getClaimByStatusTest(ClaimStatus claimStatus) {
        return claimRepository.findAllByStatus(claimStatus).stream().map(
                claim -> {

                    ClaimTestDTO claimTestDto= new ClaimTestDTO();
                    claimTestDto.setTitle(claim.getTitle());
                    claimTestDto.setEmployeeName(claim.getEmployee().getFirstname() + " " +claim.getEmployee().getLastname());
                    claimTestDto.setRequestReason(claim.getRequestReason());
                    claimTestDto.setProjectName(claim.getProject().getName());
                    claimTestDto.setCreatedTime(claim.getCreatedTime());
                    claimTestDto.setStatus(claim.getStatus());
                    claimTestDto.setId(claim.getId());
                    return claimTestDto;
                }
        ).toList();
    }

    @Override
    public ClaimTestDTO findByIdTest(UUID id) {

        Claim claim= claimRepository.findById(id).orElseThrow(()-> new RuntimeException("Claim not exist"));
        ClaimTestDTO claimTestDTO = new ClaimTestDTO();
        claimTestDTO.setEmployeeName(claim.getEmployee().getFirstname() + " " + claim.getEmployee().getLastname());
        claimTestDTO.setRequestReason(claim.getRequestReason());
        claimTestDTO.setProjectName(claim.getProject().getName());
        claimTestDTO.setTitle(claim.getTitle());
        claimTestDTO.setStatus(claim.getStatus());
        claimTestDTO.setId(claim.getId());
        claimTestDTO.setEmployeeId(claim.getEmployee().getId());
        claimTestDTO.setProjectId(claim.getProject().getId());

        ClaimDetail claimDetail = claimDetailRepository.findByClaimId(claim.getId());

        // Chuyển đổi ClaimDetail sang ClaimDetailTestDTO
        claimTestDTO.setClaimDetails(claim.getClaimDetails().stream()
                .map(detail -> {
                    ClaimDetailTestDTO detailDTO = new ClaimDetailTestDTO();
                    detailDTO.setStartTime(detail.getStartTime());
                    detailDTO.setEndTime(detail.getEndTime());
                    return detailDTO;
                })
                .collect(Collectors.toList())
        );

        // Tính tổng duration
        claimTestDTO.setDuration(claimTestDTO.calculateTotalDuration());

        return claimTestDTO;

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

            // Tính tổng duration
            long totalDurationHours = claimTestDTO.getClaimDetails().stream()
                    .filter(detailDTO -> detailDTO.getStartTime() != null && detailDTO.getEndTime() != null)
                    .mapToLong(detailDTO -> {
                        Duration duration = Duration.between(detailDTO.getStartTime(), detailDTO.getEndTime());
                        return duration.toHours(); // Tính duration bằng giờ
                    })
                    .sum();

            claim.setDuration(totalDurationHours);
            claimRepository.save(claim);

            // Lưu danh sách claimDetails
            List<ClaimDetail> details = claimTestDTO.getClaimDetails().stream()
                    .map(detailDTO -> {
                        ClaimDetail detail = new ClaimDetail();
                        detail.setStartTime(detailDTO.getStartTime());
                        detail.setEndTime(detailDTO.getEndTime());
                        detail.setClaim(claim); // Thiết lập quan hệ với Claim
                        return detail;
                    })
                    .collect(Collectors.toList());

            claim.setClaimDetails(details); // Thiết lập danh sách ClaimDetail

            claim.setClaimDetails(details); // Thiết lập danh sách ClaimDetail
            claimDetailRepository.saveAll(details);
        }
    }

    }
