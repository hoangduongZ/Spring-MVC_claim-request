package mock.claimrequest.repository;

import mock.claimrequest.entity.Employee;
import mock.claimrequest.entity.entityEnum.EmployeeStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface EmployeeRepository extends JpaRepository<Employee, UUID> {
    List<Employee> findByEmployeeStatus(EmployeeStatus employeeStatus);

    @Query("SELECT e FROM Employee e LEFT JOIN EmployeeProject ep ON e.id = ep.employee.id " +
            "WHERE (e.employeeStatus = :freeStatus OR (e.employeeStatus = :workingStatus AND ep.project.id = :projectId))")
    List<Employee> findAllFreeOrWorkingInProject(@Param("projectId") UUID projectId,
                                                 @Param("freeStatus") EmployeeStatus freeStatus,
                                                 @Param("workingStatus") EmployeeStatus workingStatus);
}
