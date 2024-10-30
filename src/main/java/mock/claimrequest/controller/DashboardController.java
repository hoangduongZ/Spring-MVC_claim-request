package mock.claimrequest.controller;

import lombok.RequiredArgsConstructor;
import mock.claimrequest.entity.Account;
import mock.claimrequest.repository.AccountRepository;
import mock.claimrequest.security.AuthService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;

@Controller
@RequestMapping("dashboard")
@RequiredArgsConstructor
public class DashboardController {
    private final AuthService authService;
    @GetMapping
    public String dashboard(Model model){
        return "dashboard/index";
    }
}
