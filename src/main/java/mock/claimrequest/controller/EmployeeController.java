package mock.claimrequest.controller;

import jakarta.validation.Valid;
import mock.claimrequest.dto.employee.EmployeeSaveDTO;
import mock.claimrequest.service.DepartmentService;
import mock.claimrequest.service.EmployeeService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import mock.claimrequest.dto.employee.EmployeeDTO;
import mock.claimrequest.entity.Employee;
import mock.claimrequest.repository.AccountRepository;
import mock.claimrequest.repository.EmployeeRepository;
import mock.claimrequest.service.DepartmentService;
import mock.claimrequest.service.EmployeeService;

@Controller
@RequestMapping("/employees")

// @CrossOrigin

public class EmployeeController {

    private EmployeeRepository repo;


    private DepartmentService departmentService ;

    private EmployeeService employeeService;

    private AccountRepository accountRepository ;

    public EmployeeController(EmployeeRepository repo, DepartmentService departmentService, EmployeeService employeeService, AccountRepository accountRepository) {
        this.repo = repo;
        this.departmentService = departmentService;
        this.employeeService = employeeService;
        this.accountRepository = accountRepository;
    }

    @GetMapping("/index")
    public String showEmployeeList(Model model){
        List<Employee> employees = repo.findAll() ;
        model.addAttribute("employees", employees) ;
        return "employee/index" ;
    }

    @GetMapping("/add")
    public String showCreatePage(Model model){
        EmployeeDTO employeeDTO = new EmployeeDTO() ; 
        model.addAttribute("employeeDTO", employeeDTO) ;
        model.addAttribute("departments", departmentService.getDepartments());
        return "employee/create" ;
    }

    @PostMapping("/add")
    public String postEmployeeAdd(@Valid @ModelAttribute EmployeeSaveDTO employeeSaveDTO, RedirectAttributes attributes){
        if(employeeService.saveEmployeeAlongWithAccount(employeeSaveDTO)){
            attributes.addFlashAttribute("message", "Save employee success");
        }
        return "redirect:/employees/add";
    }
}


