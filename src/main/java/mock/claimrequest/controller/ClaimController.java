package mock.claimrequest.controller;

import mock.claimrequest.dto.claim.ClaimGetDTO;
import mock.claimrequest.dto.claim.ClaimSaveDTO;
import mock.claimrequest.entity.entityEnum.ClaimStatus;
import mock.claimrequest.entity.entityEnum.ProjectRole;
import mock.claimrequest.service.ClaimService;
import mock.claimrequest.service.ProjectService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

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

    @PostMapping("/{status}/action")
    public String draftClaim(@ModelAttribute ClaimSaveDTO claimSaveDTO, @PathVariable("status") String status) {
        projectService.actionByStatus(ClaimStatus.valueOf(status.toUpperCase()), claimSaveDTO);
        if (status.equals("draft")) {
            return "redirect:/claims/draft";
        }
        return "redirect:/claims/index";
    }

    @GetMapping("/draft")
    public String getClaims(Model model) {
        List<ClaimGetDTO> claims = claimService.getClaimByStatus(ClaimStatus.DRAFT);
        claimService.getClaimByStatus(ClaimStatus.DRAFT);
        model.addAttribute("claims", claims);
        model.addAttribute("active", "draft");
        return "claim/draft";
    }

    @GetMapping("/index")
    public String getIndexClaim(Model model, @RequestParam(defaultValue = "pending") String status) {
        List<ClaimGetDTO> claims = claimService.getClaimByStatus(ClaimStatus.valueOf(status.toUpperCase()));
        model.addAttribute("claims", claims);
        if (claimService.getEmployeeRoleInProject().get().equals(ProjectRole.PM)) {
            model.addAttribute("role", "pm");
        } else {
            model.addAttribute("role", "normal");
        }
        model.addAttribute("active", "pending");
        model.addAttribute("currentPage", "claims");
        return "claim/index";
    }

    @GetMapping("/{id}/detail")
    public String getClaimDetail(Model model, @PathVariable UUID id) {
        model.addAttribute("claim", claimService.findById(id));
        return "claim/detail";
    }


}
