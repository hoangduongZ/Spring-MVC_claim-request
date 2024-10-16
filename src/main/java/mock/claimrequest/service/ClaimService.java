package mock.claimrequest.service;

import mock.claimrequest.dto.claim.ClaimGetDto;
import mock.claimrequest.dto.test.ClaimTestDTO;
import mock.claimrequest.entity.entityEnum.ClaimStatus;

import java.util.List;
import java.util.UUID;

public interface ClaimService {
    public List<ClaimGetDto> getClaimByStatus(ClaimStatus claimStatus);
    void paidClaim(UUID id);

    ClaimGetDto findById(UUID id);

    void cancelClaim(UUID id);

    void submitClaim(ClaimTestDTO claimTestDTO);

}
