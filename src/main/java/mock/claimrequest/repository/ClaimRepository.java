package mock.claimrequest.repository;

import mock.claimrequest.entity.Claim;
import mock.claimrequest.entity.Employee;
import mock.claimrequest.entity.Project;
import mock.claimrequest.entity.entityEnum.ClaimStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

public interface ClaimRepository extends JpaRepository<Claim, UUID> {
    List<Claim> findAllByStatus(ClaimStatus claimStatus);

    List<Claim> findAllByStatusAndEmployee(ClaimStatus status, Employee employee);

    List<Claim> findAllByStatusAndProject(ClaimStatus claimStatus, Project project);
    List<Claim> findByStatusOrderByCreatedTimeDesc(ClaimStatus status);
}
