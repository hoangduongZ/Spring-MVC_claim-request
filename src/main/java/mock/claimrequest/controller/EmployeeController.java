package mock.claimrequest.controller;

import jakarta.validation.Valid;
import mock.claimrequest.dto.employee.EmployeeGetDTO;
import mock.claimrequest.dto.employee.EmployeeListDTO;
import mock.claimrequest.dto.employee.EmployeeSaveDTO;
import mock.claimrequest.dto.employee.EmployeeUpdateDTO;
import mock.claimrequest.service.DepartmentService;
import mock.claimrequest.service.EmployeeService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.UUID;

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
    public String postEmployeeAdd(@Valid @ModelAttribute("employee") EmployeeSaveDTO employeeSaveDTO, RedirectAttributes attributes,Model model, BindingResult bindingResult){
        if (bindingResult.hasErrors()) {
            model.addAttribute("employee", employeeSaveDTO);
            model.addAttribute("departments", departmentService.findAll());
            return "employee/create";
        }
        if(employeeService.saveEmployeeAlongWithAccount(employeeSaveDTO)){
            attributes.addFlashAttribute("message", "Save employee success");
        }
        return "redirect:/employees";
    }

    @GetMapping("{id}/update")
    public String getEmployeeUpdate(Model model, @PathVariable UUID id){
        EmployeeUpdateDTO employeeUpdateDTO = employeeService.findById(id);
        model.addAttribute("employee", employeeUpdateDTO);
        model.addAttribute("departments", departmentService.findAll());
        return "employee/update";
    }

    @PostMapping("{id}/update")
    public String postEmployeeUpdate(@PathVariable UUID id,
                                     @Valid @ModelAttribute("employee") EmployeeUpdateDTO employeeUpdateDTO,
                                     BindingResult bindingResult,
                                     RedirectAttributes attributes,
                                     Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("departments", departmentService.findAll());
            return "employee/create";
        }

        try {
            employeeService.updateEmployee(id, employeeUpdateDTO);
            attributes.addFlashAttribute("successMessage", "Employee updated successfully!");
        } catch (Exception e) {
            attributes.addFlashAttribute("errorMessage", "Error updating employee: " + e.getMessage());
            return "redirect:/employees/{id}/update";
        }

        return "redirect:/employees";
    }
}
