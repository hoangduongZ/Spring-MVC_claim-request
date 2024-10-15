package mock.claimrequest.dto.auth;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AccountRegisterDTO {
    @NotBlank(message = "Username is not blank!")
    private String userName;
    @NotBlank(message = "Email is not blank!")
    private String email;
    @NotBlank(message = "Password is not blank!")
    private String password;
}
