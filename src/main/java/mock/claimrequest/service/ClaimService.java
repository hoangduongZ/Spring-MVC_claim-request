package mock.claimrequest.service;

import mock.claimrequest.dto.claim.ClaimGetDto;
import mock.claimrequest.entity.ClaimStatus;

import java.util.List;
import java.util.UUID;

public interface ClaimService {
    public List<ClaimGetDto> getClaimByStatus(ClaimStatus claimStatus);
    void paidClaim(UUID id);
}
