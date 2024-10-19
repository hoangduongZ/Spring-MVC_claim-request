package mock.claimrequest.repository;

import mock.claimrequest.entity.Claim;
import mock.claimrequest.entity.ClaimDetail;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ClaimDetailRepository extends JpaRepository<ClaimDetail, Long> {
}
