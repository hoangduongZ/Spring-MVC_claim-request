package mock.claimrequest.repository;

import mock.claimrequest.entity.ClaimDetail;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClaimDetailRepository extends JpaRepository<ClaimDetail, Long> {
}
