package mock.claimrequest.controller;

import mock.claimrequest.entity.Account;
import mock.claimrequest.repository.AccountRepository;
import mock.claimrequest.security.AuthService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AccountApiController {
    private final AuthService authService;
    private final AccountRepository accountRepository;

    public AccountApiController(AuthService authService, AccountRepository accountRepository) {
        this.authService = authService;
        this.accountRepository = accountRepository;
    }

    @GetMapping("/welcome")
    public String welcome() {
        return "Welcome "+ authService.getCurrentAccount().getUserName();
    }
}
