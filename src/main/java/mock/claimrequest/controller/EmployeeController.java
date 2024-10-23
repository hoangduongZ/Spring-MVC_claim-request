package mock.claimrequest.controller;

import jakarta.validation.Valid;
import mock.claimrequest.dto.employee.EmployeeGetDTO;
import mock.claimrequest.dto.employee.EmployeeListDTO;
import mock.claimrequest.dto.employee.EmployeeSaveDTO;
import mock.claimrequest.service.DepartmentService;
import mock.claimrequest.service.EmployeeService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("employees")
public class EmployeeController {
    private final DepartmentService departmentService;
    private final EmployeeService employeeService;

    public EmployeeController(DepartmentService departmentService, EmployeeService employeeService) {
        this.departmentService = departmentService;
        this.employeeService = employeeService;
    }

    @GetMapping
    public String getEmployees(Model model){
        model.addAttribute("employees",employeeService.getAll());
        return "employee/index";
    }

    @GetMapping("/add")
    public String getEmployeeAdd(Model model){
        model.addAttribute("employee",new EmployeeSaveDTO());
        model.addAttribute("departments", departmentService.findAll());
        return "employee/create";
    }

    @PostMapping("/add")
    public String postEmployeeAdd(@Valid @ModelAttribute EmployeeSaveDTO employeeSaveDTO, RedirectAttributes attributes){
        if(employeeService.saveEmployeeAlongWithAccount(employeeSaveDTO)){
            attributes.addFlashAttribute("message", "Save employee success");
        }
        return "redirect:/employees";
    }
}
