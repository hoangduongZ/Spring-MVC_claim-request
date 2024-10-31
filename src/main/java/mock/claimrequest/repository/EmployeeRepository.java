package mock.claimrequest.repository;

import mock.claimrequest.entity.Account;
import mock.claimrequest.entity.Employee;
import mock.claimrequest.entity.EmployeeProject;
import mock.claimrequest.entity.entityEnum.EmpProjectStatus;
import mock.claimrequest.entity.entityEnum.EmployeeStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface  EmployeeRepository extends JpaRepository<Employee, UUID> {
    Employee findByAccount(Account account);
    List<Employee> findByEmployeeStatus(EmployeeStatus employeeStatus);

    @Query("SELECT e FROM Employee e LEFT JOIN EmployeeProject ep ON e.id = ep.employee.id " +
            "WHERE (e.employeeStatus = :freeStatus OR (e.employeeStatus = :workingStatus AND ep.project.id = :projectId AND ep.empProjectStatus = :inStatus))")
    List<Employee> findAllFreeOrWorkingInProject(@Param("projectId") UUID projectId,
                                                 @Param("freeStatus") EmployeeStatus freeStatus,
                                                 @Param("workingStatus") EmployeeStatus workingStatus,
                                                 @Param("inStatus") EmpProjectStatus inStatus);

    @Query("SELECT ep FROM EmployeeProject ep LEFT JOIN ep.employee e " +
            "WHERE ep.project.id = :projectId AND ep.empProjectStatus = :outStatus")
    List<EmployeeProject> findAllEmployeesExitedFromProject(@Param("projectId") UUID projectId,
                                                            @Param("outStatus") EmpProjectStatus outStatus);


}
