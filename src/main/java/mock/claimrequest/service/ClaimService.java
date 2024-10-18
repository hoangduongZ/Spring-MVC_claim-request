package mock.claimrequest.service;



import mock.claimrequest.dto.claim.ClaimGetDTO;
import mock.claimrequest.dto.test.ClaimTestDTO;
import mock.claimrequest.entity.entityEnum.ClaimStatus;

import java.util.List;
import java.util.UUID;

public interface ClaimService {
    
    public List<ClaimGetDTO> getClaimByStatus(ClaimStatus claimStatus);

    void paidClaim(UUID id);

    ClaimGetDTO findById(UUID id);

    void cancelClaim(UUID id);


    void submitClaimById(UUID id);

    public List<ClaimTestDTO> getClaimByStatusTest(ClaimStatus claimStatus);

    ClaimTestDTO findByIdTest(UUID id);

    public void submitClaim(ClaimTestDTO claimTestDTO) ;

}
