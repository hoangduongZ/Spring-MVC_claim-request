package mock.claimrequest.controller;

import java.util.List;
import java.util.UUID;

import mock.claimrequest.service.DepartmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import mock.claimrequest.dto.account.AccountDTO;
import mock.claimrequest.dto.department.DepartmentDTO;
import mock.claimrequest.dto.employee.EmployeeDTO;
import mock.claimrequest.entity.Account;
import mock.claimrequest.entity.Employee;
import mock.claimrequest.repository.AccountRepository;
import mock.claimrequest.repository.EmployeeRepository;
import mock.claimrequest.service.EmployeeService;

@Controller
@RequestMapping("/employees")

// @CrossOrigin

public class EmployeeController {

    @Autowired
    private EmployeeRepository repo; 


    @Autowired
    private DepartmentService departmentService ;

    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private AccountRepository accountRepository ; 

    // @GetMapping("/add")
    // public String getAddEmployee() {
    //     return "employee/add";
    // }


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
    public String saveEmployee(@ModelAttribute("employeeDTO") EmployeeDTO employeeDTO) {
        employeeService.saveEmployee(employeeDTO);
        return "redirect:/employees/index" ; 
    }


//     @GetMapping("/edit/{id}")
//     public String showEditPage(@PathVariable UUID id, Model model) {
//        EmployeeDTO employeeDTO = new EmployeeDTO();
//        Employee employee = employeeService.getEmployeeById(id)
//                .orElseThrow(() -> new RuntimeException("Employee not found"));

//        // tạo giá trị cho employeeDTO từ employee
//        employeeDTO.setFirstname(employee.getFirstname());
//        employeeDTO.setLastname(employee.getLastname());
//        employeeDTO.setGender(employee.isGender());
//        employeeDTO.setDob(employee.getDob());
//        employeeDTO.setAddress(employee.getAddress());
//        employeeDTO.setDepartment(employee.getDepartment()); // gán Department cho employeeDTO(sửa lỗi)

//        model.addAttribute("employeeDTO", employeeDTO);
//        model.addAttribute("departments", departmentService.getDepartments());
//        return "employee/edit"; 


//         Account account = accountRepository.findById(employee.getId())
//                .orElseThrow(() -> new RuntimeException("Account not found"));
//        account.setUserName(employeeDTO.getAccountDTO().getUserName());
//        account.setEmail(employeeDTO.getAccountDTO().getEmail());
//        account.setPassword(employeeDTO.getAccountDTO().getPassword());

//        accountRepository.save(account);
//    }

 

}


