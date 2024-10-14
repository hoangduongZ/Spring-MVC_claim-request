package mock.claimrequest.service;

import mock.claimrequest.dto.claim.ClaimGetDto;
import mock.claimrequest.entity.ClaimStatus;

import java.util.List;

public interface ClaimService {
    public List<ClaimGetDto> getClaimByStatus(ClaimStatus claimStatus);
}
