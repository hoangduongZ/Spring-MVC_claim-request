package mock.claimrequest.controller;

import mock.claimrequest.entity.Project;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("projects")
public class ProjectController {
    @GetMapping("/add")
    public String getAddProject(){
        return "project/create";       
    }
}
