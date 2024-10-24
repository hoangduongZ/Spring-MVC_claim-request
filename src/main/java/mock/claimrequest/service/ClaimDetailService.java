package mock.claimrequest.service;

import mock.claimrequest.dto.claim.ClaimDetailDTO;

import java.util.List;
import java.util.UUID;

public interface ClaimDetailService {
    List<ClaimDetailDTO> findAllByClaimId(UUID id);
}
