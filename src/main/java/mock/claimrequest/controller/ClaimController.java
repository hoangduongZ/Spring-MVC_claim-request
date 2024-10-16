package mock.claimrequest.controller;

import mock.claimrequest.dto.claim.ClaimGetDTO;
import mock.claimrequest.entity.entityEnum.ClaimStatus;
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
    public String getClaims(Model model, @RequestParam(required = false) String active){
        model.addAttribute("currentPage", "claims");
        if(active == null || "payment".equals(active)){
            List<ClaimGetDTO> claims = claimService.getClaimByStatus(ClaimStatus.APPROVE);
            model.addAttribute("claims",claims );
            model.addAttribute("active","payment");
        }else if ("paid".equals(active)){
            List<ClaimGetDTO> claims = claimService.getClaimByStatus(ClaimStatus.PAID);
            model.addAttribute("claims",claims);
            model.addAttribute("active","paid");
        }
        return "claim/index";
    }

    @PostMapping("/{id}/paid")
    public String postClaimsPaid(RedirectAttributes attributes, @PathVariable UUID id) {
        claimService.paidClaim(id);
        return "redirect:/claims";
    }

    @GetMapping("/{id}/detail")
    public String getClaimDetail(Model model, @PathVariable UUID id) {
        model.addAttribute("claim",claimService.findById(id));
        return "claim/detail";
    }

    @GetMapping("pending")
    public String getClaims(Model model){
        List<ClaimGetDTO> claims = claimService.getClaimByStatus(ClaimStatus.PENDING);
        model.addAttribute("claims",claims);
        model.addAttribute("active","pending");
        return "claim/pending";
    }

    @PostMapping("{id}/cancel")
    public String postClaimCancel(RedirectAttributes attributes, @PathVariable UUID id){
        claimService.cancelClaim(id);
        return "claim/pending";
    }

}
