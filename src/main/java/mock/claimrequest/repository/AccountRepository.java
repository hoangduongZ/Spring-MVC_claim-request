package mock.claimrequest.repository;


import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import mock.claimrequest.entity.Account;

@Repository
public interface AccountRepository extends  JpaRepository<Account, UUID> {

    Optional<Account> findByEmployeeId(UUID employeeId) ;
    
}
