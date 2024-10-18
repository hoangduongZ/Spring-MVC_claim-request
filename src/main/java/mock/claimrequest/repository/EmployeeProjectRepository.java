package mock.claimrequest.repository;

import mock.claimrequest.entity.EmployeeProject;
import mock.claimrequest.entity.EmployeeProjectId;
import mock.claimrequest.entity.Project;
import mock.claimrequest.entity.entityEnum.EmpProjectStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface EmployeeProjectRepository extends JpaRepository<EmployeeProject, EmployeeProjectId> {
    List<EmployeeProject> findByProjectIdAndEmpProjectStatus(UUID projectId, EmpProjectStatus empProjectStatus);

    List<EmployeeProject> findByProjectId(UUID projectId);
}
