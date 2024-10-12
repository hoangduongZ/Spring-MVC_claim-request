package mock.claimrequest.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import mock.claimrequest.entity.Employee;


@Repository
public interface EmployeeRepository extends  JpaRepository<Employee, UUID> {


    
}
