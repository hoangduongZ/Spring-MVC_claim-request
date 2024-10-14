package mock.claimrequest.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import mock.claimrequest.entity.Employee;

public interface EmployeeRepository extends JpaRepository<Employee, UUID> {
}
