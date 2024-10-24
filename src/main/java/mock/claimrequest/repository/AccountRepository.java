package mock.claimrequest.repository;

import mock.claimrequest.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface AccountRepository extends JpaRepository<Account, UUID> {
    Account findByEmail(String email);
    boolean existsByEmail(String email);

    boolean existsByUserName(String username);

    @Query("SELECT a FROM Account a JOIN a.roles r WHERE r.name != 'ADMIN'")
    List<Account> findAllNonAdminAccounts();
}
