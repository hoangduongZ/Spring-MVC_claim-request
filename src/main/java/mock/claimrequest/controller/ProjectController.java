package mock.claimrequest.controller;

import mock.claimrequest.entity.Project;
import mock.claimrequest.service.EmployeeService;
import mock.claimrequest.service.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping("projects")
public class ProjectController {
    private final EmployeeService employeeService;

    public ProjectController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @GetMapping("/add")
    public String getAddProject(Model model){
        model.addAttribute("employees", employeeService);
        return "project/create";       
    }
    @Autowired
    private ProjectService projectService;

    @GetMapping("/index")
    public String getIndex(Model model, @RequestParam(value = "projectStatus", required = false) String projectStatus) {
        List<Project> projects;
        if (projectStatus != null) {
            projects = projectService.getProjectsByStatus(projectStatus);
        } else {
            projects = projectService.getAllProjects();
        }
        model.addAttribute("projects", projects);
        model.addAttribute("status", projectStatus);
        return "project/index";
    }
}
