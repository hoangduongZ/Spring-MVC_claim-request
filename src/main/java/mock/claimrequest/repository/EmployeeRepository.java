package mock.claimrequest.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import mock.claimrequest.entity.Employee;

import java.util.List;
import java.util.UUID;

public interface EmployeeRepository extends JpaRepository<Employee, UUID> {
    List<Employee> findByAccountIsNotNull();
}
