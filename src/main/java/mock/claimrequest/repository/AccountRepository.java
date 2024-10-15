package mock.claimrequest.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import mock.claimrequest.entity.Account;

public interface AccountRepository extends JpaRepository<Account, UUID> {
    Account findByEmail(String email);
    boolean existsByEmail(String email);

    boolean existsByUserName(String username);
}
