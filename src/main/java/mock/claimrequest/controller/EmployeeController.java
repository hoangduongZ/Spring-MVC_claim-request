package mock.claimrequest.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import mock.claimrequest.repository.EmployeeRepository;

@Controller
@RequestMapping("/employees")
// @CrossOrigin
public class EmployeeController {

    @Autowired
    private EmployeeRepository repo; 

    @GetMapping("/add")
    public String getAddEmployee() {
        return "employee/add";
    }
    
    
    // @GetMapping("/create")
    // public String showEmployeeList(Model model){
    //     List<Employee> employees = repo.findAll() ;
    //     model.addAttribute("employees", employees);
    //     return "employee/index" ;
    // }

}


