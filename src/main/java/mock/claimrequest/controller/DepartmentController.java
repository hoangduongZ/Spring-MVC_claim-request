package mock.claimrequest.controller;

import jakarta.validation.Valid;
import mock.claimrequest.dto.department.DepartmentSaveDTO;
import mock.claimrequest.service.DepartmentService;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("departments")
public class DepartmentController {
    private final DepartmentService departmentService;

    public DepartmentController(DepartmentService departmentService) {
        this.departmentService = departmentService;
    }

    @GetMapping
    public String getDepartment(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false, defaultValue = "name") String sortBy,
            @RequestParam(required = false, defaultValue = "desc") String order,
            @RequestParam(required = false, defaultValue = "0") Integer page,
            @RequestParam(required = false, defaultValue = "10") Integer size,
            Model model) {
        Pageable pageable = null;

        if (order.equals("asc")) {
            pageable = PageRequest.of(page, size, Sort.by(sortBy).ascending());
        } else {
            pageable = PageRequest.of(page, size, Sort.by(sortBy).descending());
        }

        var departments = departmentService.findAll(keyword, pageable);
        model.addAttribute("departments", departments);

        model.addAttribute("keyword", keyword);

        model.addAttribute("totalPages", departments.getTotalPages());

        model.addAttribute("totalElements", departments.getTotalElements());

        model.addAttribute("sortBy", sortBy);

        model.addAttribute("order", order);

        model.addAttribute("pageLimit", 3);

        model.addAttribute("page", page);

        model.addAttribute("pageSize", size);

        model.addAttribute("pageSizes", new Integer[]{10, 20, 30, 50, 100});

        model.addAttribute("currentPage", "departments");
//        // Get message from redirect
//        if (!model.containsAttribute("message")) {
//            model.addAttribute("message", new Message());
//        }
        return "department/index";
    }

    @PostMapping("/add")
    public String postSaveDepartment(RedirectAttributes redirectAttributes, @Valid @ModelAttribute DepartmentSaveDTO departmentSaveDTO) {
        if (departmentSaveDTO == null) {
            throw new IllegalStateException("Department not null");
        }
        departmentService.create(departmentSaveDTO);

//        redirectAttributes.addFlashAttribute("message", "Department created successfully");
//        redirectAttributes.addFlashAttribute("keyword", null); // Hoặc giá trị bạn muốn
//        redirectAttributes.addFlashAttribute("sortBy", "name"); // Giá trị mặc định
//        redirectAttributes.addFlashAttribute("order", "desc"); // Giá trị mặc định
//        redirectAttributes.addFlashAttribute("page", 0); // Giá trị trang đầu tiên

        return "redirect:/departments";
    }


}
