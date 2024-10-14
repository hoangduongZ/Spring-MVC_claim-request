package mock.claimrequest.controller;

import mock.claimrequest.dto.claim.ClaimGetDto;
import mock.claimrequest.entity.ClaimStatus;
import mock.claimrequest.entity.EmployeeProject;
import mock.claimrequest.service.ClaimService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.UUID;

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

    @GetMapping("/paid")
    public String getPaidNonVerify(Model model, @RequestParam(required = false) String active){
        model.addAttribute("currentPage", "paid");
        if(active == null || "pending".equals(active)){
            List<ClaimGetDto> claims = claimService.getClaimByStatus(ClaimStatus.APPROVE);
            model.addAttribute("claims",claims );
            model.addAttribute("active","pending");
        }else if ("paid".equals(active)){
            List<ClaimGetDto> claims = claimService.getClaimByStatus(ClaimStatus.PAID);
            model.addAttribute("claims",claims);
            model.addAttribute("active","paid");
        }
        return "paid/paid-non-verify";
    }

    @PostMapping("/{id}/paid")
    public String postClaimsPaid(RedirectAttributes attributes, @PathVariable UUID id) {
        claimService.paidClaim(id);
//        attributes.addFlashAttribute("active","pending");
        return "redirect:/claims/paid";
    }


}
