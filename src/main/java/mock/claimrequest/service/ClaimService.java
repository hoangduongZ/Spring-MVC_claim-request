package mock.claimrequest.service;

import mock.claimrequest.dto.claim.ClaimGetDTO;
import mock.claimrequest.entity.entityEnum.ClaimStatus;
import mock.claimrequest.entity.entityEnum.ProjectRole;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ClaimService {
    List<ClaimGetDTO> getClaimByStatus(ClaimStatus claimStatus);

    ClaimGetDTO findById(UUID id);

    Optional<ProjectRole> getEmployeeRoleInProject();
}
