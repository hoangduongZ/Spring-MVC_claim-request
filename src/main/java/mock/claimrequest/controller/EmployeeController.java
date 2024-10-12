package mock.claimrequest.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/employees")
// @CrossOrigin
public class EmployeeController {
    @GetMapping("/add")
    public String getAddEmployee() {
        return "employee/add";
    }
}


