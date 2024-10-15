package mock.claimrequest.service.impl ;

import java.util.List;
import java.util.Set;
import java.util.UUID;

import mock.claimrequest.dto.account.AccountDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import mock.claimrequest.dto.employee.EmployeeDTO;
import mock.claimrequest.entity.Account;
import mock.claimrequest.entity.Department;
import mock.claimrequest.entity.Employee;
import mock.claimrequest.repository.AccountRepository;
import mock.claimrequest.repository.DepartmentRepository;
import mock.claimrequest.repository.EmployeeRepository;
import mock.claimrequest.service.EmployeeService;


@Service
public class EmployeeServiceImpl implements EmployeeService {

    @Autowired
    private EmployeeRepository employeeRepository;
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private DepartmentRepository departmentRepository;

    
    @Override
    public List<EmployeeDTO> getAllEmployees() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getAllEmployees'");
    }

    @Override
    public EmployeeDTO getEmployeeById(UUID id) {

        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Employee not found"));

        EmployeeDTO employeeDTO = new EmployeeDTO();


        employeeDTO.setFirstname(employee.getFirstname());
        employeeDTO.setLastname(employee.getLastname());
        employeeDTO.setGender(employee.isGender());
        employeeDTO.setDob(employee.getDob());
        employeeDTO.setAddress(employee.getAddress());


        employeeDTO.setDepartment(employee.getDepartment());


        AccountDTO accountDTO = new AccountDTO();
        Set<Account> accounts = employee.getAccounts();  // Lấy tập hợp các Account

        if (accounts != null && !accounts.isEmpty()) {
            // Lấy tài khoản đầu tiên từ Set (hoặc bạn có thể thêm logic chọn tài khoản khác)
            Account account = accounts.iterator().next();

            // Map dữ liệu từ Account sang AccountDTO
            accountDTO.setUserName(account.getUserName());
            accountDTO.setEmail(account.getEmail());
            accountDTO.setPassword(account.getPassword());
        } else {
            // Xử lý khi không có Account nào liên kết với Employee
            accountDTO.setUserName("");
            accountDTO.setEmail("");
            accountDTO.setPassword("");
        }


        return employeeDTO;

    }







    @Override
    public void updateEmployee(UUID id, EmployeeDTO employeeDTO) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'updateEmployee'");
    }

    @Override
    public void deleteEmployee(UUID id) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'deleteEmployee'");
    }


    @Override
    public void saveEmployee(EmployeeDTO employeeDTO) {
       // tạo Employee từ DTO
        Employee employee = new Employee(); // tạo object
        employee.setFirstname(employeeDTO.getFirstname());   // ko có ràng buộc
        employee.setLastname(employeeDTO.getLastname());
        employee.setGender(employeeDTO.isGender());
        employee.setDob(employeeDTO.getDob());
        employee.setAddress(employeeDTO.getAddress());

        // tìm kiếm Department theo tên từ DepartmentDTO
        Department department = departmentRepository.findByName(employeeDTO.getDepartment().getName())
                .orElseThrow(() -> new RuntimeException("Department not found"));
        employee.setDepartment(department); // gán Department cho Employee

        // lưu Employee
        Employee savedEmployee = employeeRepository.save(employee);

        // tao Account từ DTO, ( phai bắt lỗi ngoại lệ )
        Account account = new Account();
        account.setUserName(employeeDTO.getAccountDTO().getUserName()); //đoạn này bị lỗi, cần thêm unique
        account.setEmail(employeeDTO.getAccountDTO().getEmail());
        account.setPassword(employeeDTO.getAccountDTO().getPassword());
        account.setEmployee(savedEmployee); // gắn Employee vào Account

        accountRepository.save(account);
    }





    
}
