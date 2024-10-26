package mock.claimrequest.dto.auth;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import mock.claimrequest.entity.Role;
import mock.claimrequest.entity.entityEnum.AccountRole;

import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AccountGetDTO {
    private UUID id;
    private String userName;
    private String email;
    private Set<Role> roles;
}
