package mock.claimrequest.controller.auth;

import jakarta.validation.Valid;
import mock.claimrequest.dto.auth.AccountRegisterDTO;
import mock.claimrequest.service.AccountService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class AccountController {
    private AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @GetMapping("/register")
    public String getRegister(Model model){
        model.addAttribute("account", new AccountRegisterDTO());
        return "auth/register";
    }

    @PostMapping("/register")
    public String postRegister(@Valid @ModelAttribute AccountRegisterDTO accountRegisterDTO){
        if (accountRegisterDTO == null){
            throw new IllegalStateException("Account not null");
        }

        if (accountService.existByUsername(accountRegisterDTO.getUserName())){
            throw new IllegalStateException("Username is existed");
        }

        if (accountService.existByEmail(accountRegisterDTO.getEmail())){
            throw new IllegalStateException("Email is existed");
        }

        accountService.register(accountRegisterDTO);
        return "redirect:/login";
    }

    @GetMapping("/login")
    public String getLogin(){
        return "auth/login";
    }
}
