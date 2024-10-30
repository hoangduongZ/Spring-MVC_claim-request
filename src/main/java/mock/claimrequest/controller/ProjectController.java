package mock.claimrequest.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.Valid;
import mock.claimrequest.dto.employeeProject.EmployeeProjectDTO;
import mock.claimrequest.dto.project.ProjectDTO;
import mock.claimrequest.dto.project.ProjectSaveDTO;
import mock.claimrequest.entity.entityEnum.ProjectRole;
import mock.claimrequest.entity.entityEnum.ProjectStatus;
import mock.claimrequest.service.EmployeeService;
import mock.claimrequest.service.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("projects")
public class ProjectController {
    private final EmployeeService employeeService;
    private final ProjectService projectService;

    @Autowired
    private ObjectMapper objectMapper;

    public ProjectController(EmployeeService employeeService, ProjectService projectService) {
        this.employeeService = employeeService;
        this.projectService = projectService;
    }

    @Secured("ROLE_ADMIN")
    @GetMapping("/add")
    public String getAddProject(Model model) throws JsonProcessingException {
        var employees = employeeService.getAllEmployeeFree();

        model.addAttribute("project", new ProjectSaveDTO());
        model.addAttribute("employees", employees);
        model.addAttribute("roles", ProjectRole.values());
//        model.addAttribute("projectStatuses", ProjectStatus.values());
        String json = objectMapper.writeValueAsString(employees);

        model.addAttribute("employeesJSON", json);
        return "project/create";       
    }


    @Secured("ROLE_ADMIN")
    @PostMapping("/add")
    public String postAddProject(@ModelAttribute @Valid ProjectSaveDTO projectSaveDTO){
        projectService.create(projectSaveDTO);
        return "redirect:/projects";
    }

    @Secured("ROLE_ADMIN")
    @GetMapping
    public String getListProject(Model model){
        model.addAttribute("projects", projectService.list());
        return "project/index";
    }

    @Secured("ROLE_ADMIN")
    @GetMapping("/{id}/edit")
    public String editProject(@PathVariable("id") UUID projectId, Model model) {
        ProjectDTO project = projectService.getById(projectId);

        List<EmployeeProjectDTO> employeeProjects = projectService.getEmployeeProjectsByProjectId(projectId);
        model.addAttribute("projectStatuses", ProjectStatus.values());

        model.addAttribute("project", project);
        model.addAttribute("employeeProjects", employeeProjects);

        var employees = employeeService.getAllEmployeeFreeAndWorkingCurrentProject(projectId);
        var employeeProjectOuts = employeeService.getAllEmployeesExitedFromProject(projectId);
        model.addAttribute("employees", employees);
        model.addAttribute("employeeProjectOuts", employeeProjectOuts);
        model.addAttribute("roles", ProjectRole.values());
        return "project/edit";
    }

    @Secured("ROLE_ADMIN")
    @PostMapping("/{id}/edit")
    public String updateProject(@ModelAttribute ProjectDTO projectDTO) {
        projectService.update(projectDTO);
        return "redirect:/projects";
    }

    @Secured("ROLE_ADMIN")
    @GetMapping("/{id}/delete")
    public String deleteProject(@PathVariable UUID id) {
        projectService.delete(id);
        return "redirect:/projects";
    }

}
