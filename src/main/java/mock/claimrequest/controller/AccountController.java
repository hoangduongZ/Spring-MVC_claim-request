package mock.claimrequest.controller;

import jakarta.validation.Valid;
import mock.claimrequest.dto.auth.AccountRegisterDTO;
import mock.claimrequest.entity.Employee;
import mock.claimrequest.security.AuthService;
import mock.claimrequest.service.AccountService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Instant;
import java.time.LocalDateTime;

@Controller
public class AccountController {
    private final AuthService authService;
    private AccountService accountService;

    public AccountController(AccountService accountService, AuthService authService) {
        this.accountService = accountService;
        this.authService = authService;
    }

    @GetMapping("/register")
    public String getRegister(Model model){
        model.addAttribute("account", new AccountRegisterDTO());
        return "auth/register";
    }

    @PostMapping("/register")
    public String postRegister(@Valid @ModelAttribute AccountRegisterDTO accountRegisterDTO){
        if (accountRegisterDTO == null){
            throw new IllegalStateException("Account not null");
        }

        if (accountService.existByUsername(accountRegisterDTO.getUserName())){
            throw new IllegalStateException("Username is existed");
        }

        if (accountService.existByEmail(accountRegisterDTO.getEmail())){
            throw new IllegalStateException("Email is existed");
        }

        accountService.register(accountRegisterDTO);
        return "redirect:/login";
    }

    @GetMapping("/login")
    public String getLogin(){
        return "auth/login";
    }

    @GetMapping("/password/change")
    public String getChangePassword(){
        return "auth/change-password";
    }

    @PostMapping("/password/change")
    public String postChangePassword(@RequestParam(name = "currentPassword") String currentPassword,
                                     @RequestParam(name = "newPassword") String newPassword,
                                     @RequestParam(name = "confirmPassword") String confirmPassword,
                                     RedirectAttributes redirectAttributes) {
        if (!newPassword.equals(confirmPassword)) {
            redirectAttributes.addFlashAttribute("error", "New password and confirmation password do not match.");
            return "redirect:/password/change";
        }

        if (!authService.checkPassword(currentPassword)) {
            redirectAttributes.addFlashAttribute("error", "Current password is incorrect.");
            return "redirect:/password/change";
        }

        authService.updatePassword(newPassword);

        redirectAttributes.addFlashAttribute("success", "Password has been changed successfully.");
        return "redirect:/password/change";
    }

    @GetMapping("/profile/avatar/update")
    public String showUpdateAvatarPage() {
        return "auth/update-avatar";
    }

    @PostMapping("/profile/avatar/update")
    public String postUpdateAvatarPage(
            @RequestParam("imageFile") MultipartFile imageFile,
            RedirectAttributes redirectAttributes,
            Model model) {

        if (imageFile == null || imageFile.isEmpty()) {
            model.addAttribute("message", "Please select an image file.");
            return "auth/update-avatar";
        }

        try {
            String imgPath = saveAvatarImage(imageFile);
            authService.updateAvatar(imgPath);

            redirectAttributes.addFlashAttribute("message", "Avatar updated successfully.");
            return "redirect:/dashboard";
        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("message", "Failed to upload image.");
            return "auth/update-avatar";
        }
    }

    private String saveAvatarImage(MultipartFile imageFile) throws IOException {
        byte[] bytes = imageFile.getBytes();
        LocalDateTime date = LocalDateTime.now();
        Path folder = Paths.get("src/main/resources/static/img/avatars/" + date.getYear() + "/"
                + date.getMonthValue() + "/" + date.getDayOfMonth());
        if (!Files.exists(folder)) {
            Files.createDirectories(folder);
        }

        String originalFileName = imageFile.getOriginalFilename();
        Long epochTime = Instant.now().getEpochSecond();
        String fileName = originalFileName.substring(0, originalFileName.lastIndexOf(".")) + "-" + epochTime
                + originalFileName.substring(originalFileName.lastIndexOf("."));
        Path path = Paths.get(folder.toString(), fileName);
        Files.write(path, bytes);

        return path.toString().replace("src\\main\\resources\\static", "");
    }


}
