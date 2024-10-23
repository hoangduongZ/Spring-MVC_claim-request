package mock.claimrequest.controller;

import mock.claimrequest.entity.Account;
import mock.claimrequest.repository.AccountRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;

@Controller
@RequestMapping("dashboard")
@SessionAttributes("userName")
public class DashboardController {

    @GetMapping
    public String dashboard(Model model){
        model.addAttribute("currentPage", "dashboard");
        return "dashboard/index";
    }
}
