package mock.claimrequest.service;

import mock.claimrequest.dto.claim.ClaimGetDTO;
import mock.claimrequest.entity.entityEnum.ClaimStatus;

import java.util.List;
import java.util.UUID;

public interface ClaimService {
    List<ClaimGetDTO> getClaimByStatus(ClaimStatus claimStatus);
    void paidClaim(UUID id);

    ClaimGetDTO findById(UUID id);

    void cancelClaim(UUID id);
}
