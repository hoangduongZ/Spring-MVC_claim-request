package mock.claimrequest.controller;

import mock.claimrequest.dto.claim.ClaimGetDTO;
import mock.claimrequest.dto.claim.ClaimSaveDTO;
import mock.claimrequest.dto.claim.ClaimUpdateStatusDTO;
import mock.claimrequest.entity.Claim;
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

    @PostMapping("{status}/add")
    public String createClaim(@ModelAttribute ClaimSaveDTO claimSaveDTO, @PathVariable("status") String status) {
        switch (status.toLowerCase()) {
            case "draft":
            case "pending":
                claimService.actionCreate(ClaimStatus.valueOf(status.toUpperCase()), claimSaveDTO);
                if (status.equals("draft")) {
                    return "redirect:/claims/draft";
                }
                break;
            default:
                return "redirect:/claims/index";
        }
        return "redirect:/claims/index";
    }

    @PostMapping("{status}/{id}")
    public String updateClaim(@ModelAttribute ClaimUpdateStatusDTO claimUpdateStatusDTO,
                              @PathVariable("status") String status,
                              @PathVariable("id") UUID id) {
        return switch (status.toLowerCase()) {
            case "approve" -> {
                claimService.actionUpdate(ClaimStatus.APPROVE, id);
                yield "redirect:/claims/index?approve";
            }
            case "reject" -> {
                claimService.actionUpdate(ClaimStatus.REJECT, id);
                yield "redirect:/claims/index?reject";
            }
            case "return" -> {
                claimService.actionUpdate(ClaimStatus.RETURN, id);
                yield "redirect:/claims/index?return";
            }
            default -> "redirect:/claims/index";
        };
    }

    @GetMapping("/draft")
    public String getClaims(Model model) {
        List<ClaimGetDTO> claims = claimService.getClaimByStatus(ClaimStatus.DRAFT);
        claimService.getClaimByStatus(ClaimStatus.DRAFT);
        model.addAttribute("claims", claims);
        model.addAttribute("active", "draft");
        return "claim/draft";
    }

    @GetMapping("/index{status}")
    public String getIndexClaim(Model model, @RequestParam(defaultValue = "pending",name = "status") String status) {
        List<ClaimGetDTO> claims = claimService.getClaimByStatus(ClaimStatus.valueOf(status.toUpperCase()));
        model.addAttribute("claims", claims);

        if (claimService.getEmployeeRoleInProject().isPresent()) {
            ProjectRole role = claimService.getEmployeeRoleInProject().get();
            model.addAttribute("role", role == ProjectRole.PM ? "pm" : "normal");
        }

        model.addAttribute("active", status.toLowerCase());
        model.addAttribute("currentPage", "claims");
        return "claim/index";
    }

    @GetMapping("/{id}/detail")
    public String getClaimDetail(Model model, @PathVariable UUID id) {
        model.addAttribute("claim", claimService.findById(id));
        return "claim/detail";
    }


}
