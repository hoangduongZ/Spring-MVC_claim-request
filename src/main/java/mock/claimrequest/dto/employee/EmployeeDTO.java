package mock.claimrequest.dto.employee;

import java.time.LocalDate;

import org.hibernate.validator.constraints.Length;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import mock.claimrequest.dto.account.AccountDTO;
import mock.claimrequest.entity.Department;


@Getter
@Setter
public class EmployeeDTO {
    
    @NotBlank(message = "firstname is required")
    @Length(min = 8, max = 50, message = "Confirm password must be between 8 and 50 characters")
    private String firstname;
    

    @NotBlank(message = "lastname is required")
    @Length(min = 8, max = 50, message = "Confirm password must be between 8 and 50 characters")
    private String lastname;


    @NotNull(message = "gender is required" )
    private boolean gender;

    @NotNull(message = "Date of birth is required" )
    private LocalDate dob;

    @NotBlank(message = "address is required")
    @Length(min = 8, max = 50, message = "Confirm password must be between 8 and 50 characters")
    private String address;

    private Department department;

    private AccountDTO accountDTO ;

    
}
