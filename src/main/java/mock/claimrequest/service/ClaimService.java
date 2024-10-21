package mock.claimrequest.service;

import mock.claimrequest.dto.claim.ClaimGetDTO;
import mock.claimrequest.dto.claim.ClaimSaveDTO;
import mock.claimrequest.entity.entityEnum.ClaimStatus;
import mock.claimrequest.entity.entityEnum.ProjectRole;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ClaimService {
    List<ClaimGetDTO> getClaimByStatus(ClaimStatus claimStatus);

    ClaimGetDTO findById(UUID id);

    Optional<ProjectRole> getEmployeeRoleInProject();

    void actionCreate(ClaimStatus claimStatus, ClaimSaveDTO claimSaveDTO);

    void updateStatus(ClaimStatus claimStatus, UUID id);

    void update();
}
