package mock.claimrequest.repository;

import mock.claimrequest.entity.EmployeeProject;
import mock.claimrequest.entity.EmployeeProjectId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EmployeeProjectRepository extends JpaRepository<EmployeeProject, EmployeeProjectId> {
}
