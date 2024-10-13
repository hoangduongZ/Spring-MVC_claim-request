package mock.claimrequest.service.impl;

import mock.claimrequest.entity.Claim;
import mock.claimrequest.entity.ClaimStatus;
import mock.claimrequest.repository.ClaimRepository;
import mock.claimrequest.service.ClaimService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClaimServiceImpl implements ClaimService {
    private final ClaimRepository claimRepository;

    public ClaimServiceImpl(ClaimRepository claimRepository) {
        this.claimRepository = claimRepository;
    }

    public List<Claim> getClaimPending(){
        List<Claim> claims= claimRepository.findAllByStatus(ClaimStatus.PENDING);
        return null;
    }
}
