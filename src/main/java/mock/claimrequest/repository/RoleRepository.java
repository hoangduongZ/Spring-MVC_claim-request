package mock.claimrequest.repository;

import mock.claimrequest.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface RoleRepository extends JpaRepository<Role, UUID> {
    boolean existsByName(String role);

    Role findByName(String role);
}
