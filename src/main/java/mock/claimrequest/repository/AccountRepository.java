package mock.claimrequest.repository;

import mock.claimrequest.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface AccountRepository extends JpaRepository<Account, UUID> {
    Account findByEmail(String email);
    boolean existsByEmail(String email);

    boolean existsByUserName(String username);
}
