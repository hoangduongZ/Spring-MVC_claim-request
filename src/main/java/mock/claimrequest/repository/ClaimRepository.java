package mock.claimrequest.repository;

import mock.claimrequest.entity.Claim;
import mock.claimrequest.entity.ClaimStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface ClaimRepository extends JpaRepository<Claim, UUID> {
    List<Claim> findAllByStatus(ClaimStatus claimStatus);
}
