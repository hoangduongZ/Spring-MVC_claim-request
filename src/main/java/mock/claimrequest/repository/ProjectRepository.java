package mock.claimrequest.repository;

import mock.claimrequest.entity.Project;
import mock.claimrequest.entity.entityEnum.ProjectStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ProjectRepository extends JpaRepository<Project, UUID> {
    List<Project> findAllByProjectStatus(ProjectStatus projectStatus);

    @Query("SELECT p FROM Project p JOIN EmployeeProject ep ON p.id = ep.project.id WHERE ep.employee.id = :employeeId")
    List<Project> findProjectsByEmployeeId(UUID employeeId);

    @Query("SELECT ep.project FROM EmployeeProject ep WHERE ep.employee.id = :employeeId AND ep.empProjectStatus = 'IN'")
    List<Project> findActiveProjectsByEmployeeId(UUID employeeId);

    Optional<Project> findByName(String name);

}
