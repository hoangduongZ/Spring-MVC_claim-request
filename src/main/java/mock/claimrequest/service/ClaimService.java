package mock.claimrequest.service;

import mock.claimrequest.dto.claim.ClaimGetDTO;
import mock.claimrequest.dto.claim.ClaimSaveDTO;
import mock.claimrequest.entity.entityEnum.ClaimStatus;
import mock.claimrequest.entity.entityEnum.ProjectRole;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ClaimService {
    Page<ClaimGetDTO> getClaimByStatusAndKeyword(ClaimStatus status, String keyword, LocalDate startDate, LocalDate endDate, Pageable pageable);

    List<ClaimGetDTO> getClaimByStatus(ClaimStatus claimStatus);

    ClaimGetDTO findById(UUID id);

    Optional<ProjectRole> getEmployeeRoleInProject();

    void actionCreate(ClaimStatus claimStatus, ClaimSaveDTO claimSaveDTO);

    void updateStatus(ClaimStatus claimStatus, UUID id);

    void update(ClaimGetDTO claimGetDTO, UUID id, String status);
}
