package mock.claimrequest.dto.account;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AccountDTO {

    @NotBlank(message = "username is required")
    private String userName;
    
    
    @NotBlank(message = "email is required")
    private String email;
    

    @NotBlank(message = "password is required")
    private String password;
    
}
