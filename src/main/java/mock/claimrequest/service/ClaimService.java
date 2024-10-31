package mock.claimrequest.service;

import mock.claimrequest.dto.claim.ClaimGetDTO;
import mock.claimrequest.dto.claim.ClaimSaveDTO;
import mock.claimrequest.dto.claim.ClaimUpdateStatusDTO;
import mock.claimrequest.entity.Claim;
import mock.claimrequest.entity.entityEnum.ClaimStatus;
import mock.claimrequest.entity.entityEnum.ProjectRole;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public interface ClaimService {
    Page<ClaimGetDTO> getClaimByStatusAndKeyword(ClaimStatus status, String keyword, LocalDateTime startDate, LocalDateTime endDate, Pageable pageable);

    List<ClaimGetDTO> getClaimByStatus(ClaimStatus claimStatus);

    ClaimGetDTO findById(UUID id);

    Optional<ProjectRole> getEmployeeRoleInProject();

    Claim actionCreate(ClaimStatus claimStatus, ClaimSaveDTO claimSaveDTO);

    void updateStatus(ClaimStatus claimStatus, UUID id, ClaimUpdateStatusDTO claimUpdateStatusDTO);

    Claim update(ClaimGetDTO claimGetDTO, UUID id, String status);

    ByteArrayOutputStream exportClaimsToExcel(List<UUID> claimIds) throws IOException;

    Long countByStatus(ClaimStatus claimStatus);

    Map<LocalDate, Long> countClaimsByDate();
}
