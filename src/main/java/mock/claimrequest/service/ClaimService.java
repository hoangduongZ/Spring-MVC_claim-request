package mock.claimrequest.service;

<<<<<<< HEAD
import mock.claimrequest.dto.claim.ClaimGetDto;
import mock.claimrequest.dto.test.ClaimTestDTO;
=======
import mock.claimrequest.dto.claim.ClaimGetDTO;
>>>>>>> dev
import mock.claimrequest.entity.entityEnum.ClaimStatus;

import java.util.List;
import java.util.UUID;

public interface ClaimService {
    public List<ClaimGetDTO> getClaimByStatus(ClaimStatus claimStatus);
    void paidClaim(UUID id);

    ClaimGetDTO findById(UUID id);

    void cancelClaim(UUID id);

    void submitClaim(ClaimTestDTO claimTestDTO);

}
