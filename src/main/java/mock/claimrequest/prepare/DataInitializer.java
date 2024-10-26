package mock.claimrequest.prepare;

import mock.claimrequest.entity.Role;
import mock.claimrequest.entity.entityEnum.AccountRole;
import mock.claimrequest.repository.RoleRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
public class DataInitializer implements CommandLineRunner {

    private RoleRepository roleRepository;

    public DataInitializer(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        createRolesIfNotExists();
    }

    private void createRolesIfNotExists() {
        List<Role> roles = Arrays.asList(
                new Role(AccountRole.CLAIMER),
                new Role(AccountRole.ADMIN),
                new Role(AccountRole.APPROVER),
                new Role(AccountRole.FINANCE)
        );

        for (Role role : roles) {
            if (!roleRepository.existsByName(role.getName())) {
                roleRepository.save(role);
            }
        }
    }
}
