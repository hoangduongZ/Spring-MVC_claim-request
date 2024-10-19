package mock.claimrequest.repository;

import mock.claimrequest.entity.Role;
import mock.claimrequest.entity.entityEnum.AccountRole;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface RoleRepository extends JpaRepository<Role, UUID> {
    boolean existsByName(AccountRole role);

    Role findByName(AccountRole role);
}
