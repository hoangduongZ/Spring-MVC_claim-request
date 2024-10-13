package mock.claimrequest.controller;

import mock.claimrequest.entity.EmployeeProject;
import mock.claimrequest.service.ClaimService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("claims")
public class ClaimController {
    private final ClaimService claimService;

    public ClaimController(ClaimService claimService) {
        this.claimService = claimService;
    }

    @GetMapping("/add")
    public String getCreate(){
        return "claim/create";
    }

    @GetMapping
    public String getClaims(Model model) {
//        List<EmployeeProject> projects =
        model.addAttribute("currentPage", "claims");
        return "claim/index";
    }
}
