package mock.claimrequest.controller;

import mock.claimrequest.dto.claim.ClaimGetDTO;
import mock.claimrequest.dto.claim.ClaimSaveDTO;
import mock.claimrequest.entity.entityEnum.ClaimStatus;
import mock.claimrequest.service.ClaimService;
import mock.claimrequest.service.ProjectService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("claims")
public class ClaimController {
    private final ClaimService claimService;
    private final ProjectService projectService;

    public ClaimController(ClaimService claimService, ProjectService projectService) {
        this.claimService = claimService;
        this.projectService = projectService;
    }

    @GetMapping("/add")
    public String showAddClaimForm(Model model) {
        model.addAttribute("claim", new ClaimSaveDTO());
        model.addAttribute("projects", projectService.getProjectForClaim(UUID.fromString("eb76b80b-364b-4ef5-a93b-96a11a47da92")));
        return "claim/create";
    }

    @PostMapping("/{action}")
    public String draftClaim(@ModelAttribute ClaimSaveDTO claimSaveDTO, @PathVariable("action") String action) {
        projectService.actionByStatus(ClaimStatus.valueOf(action.toUpperCase()), claimSaveDTO);
        if (action.equals("draft")){
            return "redirect:/claims/draft";
        }
        return null;
    }

    @GetMapping("/{action}")
    public String getClaims(Model model,@PathVariable String action) {
        List<ClaimGetDTO> claims = claimService.getClaimByStatus(ClaimStatus.valueOf(action.toUpperCase()));
        model.addAttribute("claims", claims);
        model.addAttribute("active", action);
        return "claim/draft";
    }

    @PostMapping("/{id}/paid")
    public String postClaimsPaid(RedirectAttributes attributes, @PathVariable UUID id) {
        claimService.paidClaim(id);
        return "redirect:/claims";
    }

    @GetMapping("/{id}/detail")
    public String getClaimDetail(Model model, @PathVariable UUID id) {
        model.addAttribute("claim", claimService.findById(id));
        return "claim/detail";
    }



    @PostMapping("{id}/cancel")
    public String postClaimCancel(RedirectAttributes attributes, @PathVariable UUID id) {
        claimService.cancelClaim(id);
        return "claim/pending";
    }

}
