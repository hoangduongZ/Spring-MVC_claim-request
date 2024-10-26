package mock.claimrequest.service.impl;

import mock.claimrequest.dto.claim.ClaimDetailDTO;
import mock.claimrequest.dto.claim.ClaimExportDTO;
import mock.claimrequest.dto.claim.ClaimGetDTO;
import mock.claimrequest.dto.claim.ClaimSaveDTO;
import mock.claimrequest.dto.project.ProjectDTO;
import mock.claimrequest.dto.project.ProjectGetDTO;
import mock.claimrequest.entity.Claim;
import mock.claimrequest.entity.ClaimDetail;
import mock.claimrequest.entity.Employee;
import mock.claimrequest.entity.EmployeeProject;
import mock.claimrequest.entity.EmployeeProjectId;
import mock.claimrequest.entity.Project;
import mock.claimrequest.entity.entityEnum.AccountRole;
import mock.claimrequest.entity.entityEnum.ClaimStatus;
import mock.claimrequest.entity.entityEnum.EmpProjectStatus;
import mock.claimrequest.entity.entityEnum.ProjectRole;
import mock.claimrequest.entity.entityEnum.ProjectStatus;
import mock.claimrequest.exception.ProjectNotInProgressException;
import mock.claimrequest.repository.ClaimDetailRepository;
import mock.claimrequest.repository.ClaimRepository;
import mock.claimrequest.repository.EmployeeProjectRepository;
import mock.claimrequest.repository.EmployeeRepository;
import mock.claimrequest.repository.ProjectRepository;
import mock.claimrequest.security.AuthService;
import mock.claimrequest.service.ClaimService;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class ClaimServiceImpl implements ClaimService {
    private final ClaimRepository claimRepository;
    private final EmployeeRepository employeeRepository;
    private final EmployeeProjectRepository employeeProjectRepository;
    private final AuthService authService;
    private final ProjectRepository projectRepository;
    private final ClaimDetailRepository claimDetailRepository;
    private final ModelMapper modelMapper;

    public ClaimServiceImpl(ClaimRepository claimRepository, EmployeeRepository employeeRepository, EmployeeProjectRepository employeeProjectRepository, AuthService authService, ProjectRepository projectRepository, ClaimDetailRepository claimDetailRepository, ModelMapper modelMapper) {
        this.claimRepository = claimRepository;
        this.employeeRepository = employeeRepository;
        this.employeeProjectRepository = employeeProjectRepository;
        this.authService = authService;
        this.projectRepository = projectRepository;
        this.claimDetailRepository = claimDetailRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public Page<ClaimGetDTO> getClaimByStatusAndKeyword(ClaimStatus status, String keyword, LocalDate startDate, LocalDate endDate, Pageable pageable) {
        AccountRole currentRole = authService.getCurrentRoleAccount();

        if (currentRole == AccountRole.ADMIN) {
            return handleAdminClaims(status, keyword, startDate, endDate, pageable);
        }

        Employee employee = authService.getCurrentAccount().getEmployee();

        if (currentRole == AccountRole.FINANCE) {
            return handleFinanceClaims(status, keyword, startDate, endDate, pageable);
        }

        if (currentRole == AccountRole.APPROVER) {
            return handleApproverClaims(status, keyword, startDate, endDate, employee, pageable);
        }

        return handleDefaultClaims(status, keyword, startDate, endDate, employee, pageable);
    }

    private Page<ClaimGetDTO> handleAdminClaims(ClaimStatus status, String keyword, LocalDate startDate, LocalDate endDate, Pageable pageable) {
        if ((keyword != null && !keyword.trim().isEmpty()) || (startDate != null && endDate != null)) {
            return claimRepository.findByStatusKeywordAndDateRange(status, keyword, null, startDate, endDate, pageable)
                    .map(this::convertToDTO);
        }
        return claimRepository.findByStatus(status, pageable).map(this::convertToDTO);
    }

    private Page<ClaimGetDTO> handleFinanceClaims(ClaimStatus status, String keyword, LocalDate startDate, LocalDate endDate, Pageable pageable) {
        if ((keyword != null && !keyword.trim().isEmpty()) || (startDate != null && endDate != null)) {
            return claimRepository.findByStatusKeywordAndDateRange(status, keyword, null, startDate, endDate, pageable)
                    .map(this::convertToDTO);
        }
        return claimRepository.findByStatus(status, pageable).map(this::convertToDTO);
    }

    private Page<ClaimGetDTO> handleApproverClaims(ClaimStatus status, String keyword, LocalDate startDate, LocalDate endDate, Employee employee, Pageable pageable) {
        UUID employeeId = authService.getCurrentAccount().getEmployee().getId();
        UUID projectId = employeeProjectRepository.findByEmployeeIdAndEmpProjectStatus(employeeId, EmpProjectStatus.IN).getProject().getId();

        if ((keyword != null && !keyword.trim().isEmpty()) || (startDate != null && endDate != null)) {
            return claimRepository.findByStatusKeywordAndDateRangeAndProjectId(status, keyword, projectId, startDate, endDate, pageable)
                    .map(this::convertToDTO);
        }
        return claimRepository.findByStatusAndProject(status, projectId, pageable).map(this::convertToDTO);
    }

    private Page<ClaimGetDTO> handleDefaultClaims(ClaimStatus status, String keyword, LocalDate startDate, LocalDate endDate, Employee employee, Pageable pageable) {
        if ((keyword == null || keyword.trim().isEmpty()) && (startDate == null || endDate == null)) {
            return claimRepository.findByStatusAndEmployee(status, employee, pageable).map(this::convertToDTO);
        }
        return claimRepository.findByStatusKeywordAndDateRange(status, keyword, employee, startDate, endDate, pageable)
                .map(this::convertToDTO);
    }

    @Override
    public List<ClaimGetDTO> getClaimByStatus(ClaimStatus claimStatus) {
        AccountRole currentRole = authService.getCurrentRoleAccount();

        if (!Objects.equals(currentRole,AccountRole.ADMIN)) {
            if (currentRole == AccountRole.FINANCE) {
                return claimRepository.findAllByStatus(ClaimStatus.APPROVED).stream()
                        .map(this::convertToDTO)
                        .toList();
            }

            UUID employeeId = authService.getCurrentAccount().getEmployee().getId();
            EmployeeProject employeeProject = employeeProjectRepository.findByEmployeeIdAndEmpProjectStatus(employeeId, EmpProjectStatus.IN);
            if(employeeProject == null){
                return null;
            }

            return getEmployeeRoleInProject()
                    .map(role -> {
                        if (role == ProjectRole.PM) {
                            return getClaimsByStatusAndProject(claimStatus, employeeProject.getProject().getId());
                        } else {
                            return getClaimsByStatusAndEmployee(claimStatus, employeeId);
                        }
                    })
                    .orElseGet(() -> getClaimsByStatusAndEmployee(claimStatus, employeeId));
        }

        return claimRepository.findAllByStatus(claimStatus).stream().map(this::convertToDTO).toList();
    }

    @Override
    public Optional<ProjectRole> getEmployeeRoleInProject() {
        UUID employeeId = authService.getCurrentAccount().getEmployee().getId();
        EmployeeProject employeeProject = employeeProjectRepository.findByEmployeeIdAndEmpProjectStatus(employeeId, EmpProjectStatus.IN);
        return employeeProjectRepository.findByEmployeeIdAndProjectId(employeeId, employeeProject.getProject().getId())
                .map(EmployeeProject::getRole);
    }

    private List<ClaimGetDTO> getClaimsByStatusAndProject(ClaimStatus claimStatus, UUID projectId) {
        Project project= projectRepository.findById(projectId).orElseThrow(()
        -> new IllegalStateException("Project not existed"));
        return claimRepository.findAllByStatusAndProject(claimStatus, project).stream()
                .map(this::convertToDTO)
                .toList();
    }

    private List<ClaimGetDTO> getClaimsByStatusAndEmployee(ClaimStatus claimStatus, UUID employeeId) {
        Employee employee = employeeRepository.findById(employeeId).orElseThrow(
                () -> new IllegalStateException("Employee not found!"));
        return claimRepository.findAllByStatusAndEmployee(claimStatus, employee).stream()
                .map(this::convertToDTO)
                .toList();
    }

    private ClaimGetDTO convertToDTO(Claim claim) {
        ClaimGetDTO claimGetDto = new ClaimGetDTO();
        claimGetDto.setEmployeeName(claim.getEmployee().getFirstname() + " " + claim.getEmployee().getLastname());
        claimGetDto.setRequestReason(claim.getRequestReason());
        claimGetDto.setProjectName(claim.getProject().getName());
        claimGetDto.setCreatedTime(claim.getCreatedTime());
        claimGetDto.setUpdatedTime(claim.getUpdatedTime());
        claimGetDto.setAmount(claim.getAmount());
        claimGetDto.setStatus(claim.getStatus());
        claimGetDto.setId(claim.getId());
        claimGetDto.setTitle(claim.getTitle());
        claimGetDto.setClaimDetailDTOList(
                claim.getClaimDetails().stream()
                        .map(claimDetail -> modelMapper.map(claimDetail, ClaimDetailDTO.class))
                        .toList()
        );
        claimGetDto.setReturnReason(claim.getReturnReason());
        claimGetDto.setRejectReason(claim.getRejectReason());
        claimGetDto.setAccount(claim.getEmployee().getAccount());
        ProjectDTO project = modelMapper.map(claim.getProject(),ProjectDTO.class);
        claimGetDto.setProject(project);
        claimGetDto.setDuration(claim.getDuration());
        return claimGetDto;
    }

    @Override
    public ClaimGetDTO findById(UUID id) {
        Claim claim= claimRepository.findById(id).orElseThrow(()-> new RuntimeException("Claim not exist"));
        return convertToDTO(claim);
    }

    @Override
    public void actionCreate(ClaimStatus claimStatus, ClaimSaveDTO claimSaveDTO) {
        Employee employee = employeeRepository.findByAccount(authService.getCurrentAccount());
        Project project = projectRepository.findById(claimSaveDTO.getProjectGetDTO().getId()).orElseThrow(()-> new IllegalStateException("Project not existed"));
        EmployeeProject employeeProject = employeeProjectRepository.findById(new EmployeeProjectId(employee.getId(), project.getId())).orElseThrow(
                ()->new IllegalStateException("EmployeeProject not existed")
        );
        if (!project.getProjectStatus().equals(ProjectStatus.IN_PROGRESS)){
            throw new ProjectNotInProgressException();
        }

        Claim claim= new Claim();
        if (claimSaveDTO.getDuration().isEmpty()){
            claimSaveDTO.setDuration("0");
        }
        claim.setProject(project);
        claim.setTitle(claimSaveDTO.getTitle());
        claim.setRequestReason(claimSaveDTO.getRequestReason());
        claim.setDuration(Double.parseDouble(claimSaveDTO.getDuration()));
        claim.setStatus(claimStatus);
        claim.setEmployee(employee);
        claim.setAmount(BigDecimal.valueOf(employeeProject.getRole().getSalary() * claim.getDuration()));
        claimRepository.save(claim);

        if (claimSaveDTO.getClaimDetails() != null && !claimSaveDTO.getClaimDetails().isEmpty()) {
            List<ClaimDetail> claimDetails = claimSaveDTO.getClaimDetails().stream()
                    .map(detailDTO -> {
                        ClaimDetail claimDetail = new ClaimDetail();
                        claimDetail.setClaim(claim);
                        claimDetail.setStartTime(detailDTO.getStartTime());
                        claimDetail.setEndTime(detailDTO.getEndTime());
                        return claimDetail;
                    })
                    .collect(Collectors.toList());

            claimDetailRepository.saveAll(claimDetails);
        }
    }

    @Override
    public void updateStatus(ClaimStatus claimStatus, UUID id) {
        Claim claim = claimRepository.findById(id).orElseThrow(
                () -> new IllegalStateException("Claim is not existed!"));
        claim.setStatus(claimStatus);
        claimRepository.save(claim);
    }

    @Override
    public void update(ClaimGetDTO claimGetDTO, UUID id, String status) {
        Claim claim = claimRepository.findById(id)
                .orElseThrow(() -> new IllegalStateException("Claim is not existed!"));
        Employee employee = employeeRepository.findByAccount(authService.getCurrentAccount());
        Project project = projectRepository.findById(claimGetDTO.getProject().getId()).orElseThrow(()-> new IllegalStateException("Project not existed"));
        if (!project.getProjectStatus().equals(ProjectStatus.IN_PROGRESS)){
            throw new ProjectNotInProgressException();
        }
        EmployeeProject employeeProject = employeeProjectRepository.findById(new EmployeeProjectId(employee.getId(), project.getId())).orElseThrow(
                ()->new IllegalStateException("EmployeeProject not existed")
        );
        claim.setTitle(claimGetDTO.getTitle());
        claim.setRequestReason(claimGetDTO.getRequestReason());
        claim.setAmount(BigDecimal.valueOf(employeeProject.getRole().getSalary() * claim.getDuration()));
        claim.setDuration(claimGetDTO.getDuration());

        claim.setStatus(ClaimStatus.valueOf(status.toUpperCase()));
        claim.setUpdatedTime(LocalDateTime.now());

        List<ClaimDetail> claimDetails = new ArrayList<>();
        if (claimGetDTO.getClaimDetailDTOList()!= null){
            claimDetails = claimGetDTO.getClaimDetailDTOList().stream()
                    .map(detailDTO -> {
                        ClaimDetail claimDetail = new ClaimDetail();
                        claimDetail.setStartTime(detailDTO.getStartTime());
                        claimDetail.setEndTime(detailDTO.getEndTime());
                        claimDetail.setClaim(claim);
                        return claimDetail;
                    }).toList();
        }

        claim.getClaimDetails().clear();
        claim.getClaimDetails().addAll(claimDetails);

        claimRepository.save(claim);
    }

    public ByteArrayOutputStream exportClaimsToExcel(List<UUID> claimIds) throws IOException {
        List<Claim> claims = claimRepository.findAllById(claimIds);

        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Claims");

        // Tạo style cho tiêu đề chính
        CellStyle titleStyle = workbook.createCellStyle();
        titleStyle.setAlignment(HorizontalAlignment.CENTER);
        titleStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        Font titleFont = workbook.createFont();
        titleFont.setBold(true);
        titleFont.setFontHeightInPoints((short) 14);
        titleStyle.setFont(titleFont);

        // Tạo tiêu đề chính
        Row titleRow = sheet.createRow(0);
        Cell titleCell = titleRow.createCell(0);
        titleCell.setCellValue("DANH SÁCH YÊU CẦU CLAIM REQUEST");
        titleCell.setCellStyle(titleStyle);

        // Hợp nhất các ô để tiêu đề kéo dài toàn bộ bảng
        sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 5));

        // Thêm dòng thông tin chi tiết (nếu cần)
        Row subtitleRow = sheet.createRow(1);
        Cell subtitleCell = subtitleRow.createCell(0);
        subtitleCell.setCellValue("Dự kiến cấp vào: " + "tháng" + " " + "năm" + "2024");
        subtitleCell.setCellStyle(titleStyle);

        // Hợp nhất các ô cho dòng thông tin chi tiết
        sheet.addMergedRegion(new CellRangeAddress(1, 1, 0, 5));

        // Tạo style cho header
        CellStyle headerStyle = workbook.createCellStyle();
        headerStyle.setFillForegroundColor(IndexedColors.LIGHT_BLUE.getIndex());
        headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        headerStyle.setAlignment(HorizontalAlignment.CENTER);
        headerStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        Font headerFont = workbook.createFont();
        headerFont.setBold(true);
        headerFont.setFontHeightInPoints((short) 12);
        headerStyle.setFont(headerFont);

        // Tạo header row với style
        Row headerRow = sheet.createRow(2); // Bắt đầu từ dòng 3
        String[] headers = {"ID", "Employee Name", "Project Name", "Title", "Reason", "Amount"};
        for (int i = 0; i < headers.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(headers[i]);
            cell.setCellStyle(headerStyle);
        }

        // Style cho các ô thông tin
        CellStyle cellStyle = workbook.createCellStyle();
        cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        cellStyle.setWrapText(true);

        CellStyle amountStyle = workbook.createCellStyle();
        amountStyle.cloneStyleFrom(cellStyle);
        amountStyle.setDataFormat(workbook.createDataFormat().getFormat("$#,##0.00"));

        // Điền dữ liệu vào các hàng
        int rowIndex = 3;
        for (Claim claim : claims) {
            Row row = sheet.createRow(rowIndex++);
            row.createCell(0).setCellValue(claim.getId().toString());
            row.createCell(1).setCellValue(claim.getEmployee().getAccount().getUserName());
            row.createCell(2).setCellValue(claim.getProject().getName());
            row.createCell(3).setCellValue(claim.getTitle());
            row.createCell(4).setCellValue(claim.getRequestReason());

            // Dùng style định dạng số tiền
            Cell amountCell = row.createCell(5);
            amountCell.setCellValue(claim.getAmount().doubleValue());
            amountCell.setCellStyle(amountStyle);
        }

        // Tự động điều chỉnh kích thước các cột
        for (int i = 0; i < headers.length; i++) {
            sheet.autoSizeColumn(i);
        }

        // Ghi vào output stream
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        workbook.write(outputStream);
        workbook.close();

        return outputStream;
    }

}
