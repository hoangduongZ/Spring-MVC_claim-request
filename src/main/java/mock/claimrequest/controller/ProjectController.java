package mock.claimrequest.controller;

import mock.claimrequest.dto.project.ProjectSaveDTO;
import mock.claimrequest.entity.Project;
import mock.claimrequest.entity.entityEnum.ProjectRole;
import mock.claimrequest.service.EmployeeService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("projects")
public class ProjectController {
    private final EmployeeService employeeService;

    public ProjectController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @GetMapping("/add")
    public String getAddProject(Model model){
        model.addAttribute("project", new ProjectSaveDTO());
        model.addAttribute("employees", employeeService.getAll());
        model.addAttribute("roles", ProjectRole.values());
        return "project/create";       
    }
}
