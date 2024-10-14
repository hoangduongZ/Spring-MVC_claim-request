package mock.claimrequest.service.impl;

import mock.claimrequest.dto.claim.ClaimGetDto;
import mock.claimrequest.entity.Claim;
import mock.claimrequest.entity.ClaimStatus;
import mock.claimrequest.entity.Employee;
import mock.claimrequest.repository.ClaimRepository;
import mock.claimrequest.repository.EmployeeRepository;
import mock.claimrequest.service.ClaimService;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;

@Service
public class ClaimServiceImpl implements ClaimService {
    private final ClaimRepository claimRepository;
    private final EmployeeRepository employeeRepository;

    public ClaimServiceImpl(ClaimRepository claimRepository, EmployeeRepository employeeRepository) {
        this.claimRepository = claimRepository;
        this.employeeRepository = employeeRepository;
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
