package mock.claimrequest.dto.auth;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AccountChangePasswordDTO {
    private String currentPassword;
    private String newPassword;
}
