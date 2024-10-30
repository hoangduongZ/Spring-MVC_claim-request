package mock.claimrequest.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import mock.claimrequest.dto.auth.AccountRegisterDTO;
import mock.claimrequest.entity.Employee;
import mock.claimrequest.security.AuthService;
import mock.claimrequest.security.TokenCache;
import mock.claimrequest.service.AccountService;
import mock.claimrequest.service.EmailService;
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
    private final EmailService emailService;
    private final TokenCache tokenCache;

    public AccountController(AccountService accountService, AuthService authService, EmailService emailService, TokenCache tokenCache) {
        this.accountService = accountService;
        this.authService = authService;
        this.emailService = emailService;
        this.tokenCache = tokenCache;
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

    @GetMapping("/password/forgot")
    public String getForgetPassword(Model model) {
        return "auth/forgot-password";
    }

    @PostMapping("/password/forgot")
    public String postForgetPassword(@RequestParam String email, RedirectAttributes attributes, HttpServletRequest request) {
        if (!accountService.existByEmail(email)) {
            attributes.addFlashAttribute("email", email);
            attributes.addFlashAttribute("error", "Email not existed!");
            return "redirect:/password/forgot";
        }

         emailService.sendResetLink(request, email);

        attributes.addFlashAttribute("email", email);
        attributes.addFlashAttribute("success", "We sent a link to your email, please check your email: " + email);
        return "redirect:/password/forgot";
    }

    @GetMapping("/password/reset")
    public String getResetPassword(@RequestParam String token, Model model) {
        if (!tokenCache.exists(token)) {
            model.addAttribute("error", "Invalid or expired token.");
            return "auth/reset-password";
        }
        model.addAttribute("token", token);
        return "auth/reset-password";
    }

    @PostMapping("/password/reset")
    public String postResetPassword(@RequestParam String token, @RequestParam String newPassword, RedirectAttributes attributes) {
        String email = tokenCache.get(token);

        if (email == null) {
            attributes.addFlashAttribute("error", "Invalid or expired token.");
            return "redirect:/password/reset";
        }

        accountService.updatePassword(email, newPassword);
        tokenCache.remove(token);

        attributes.addFlashAttribute("success", "Your password has been reset successfully.");
        return "redirect:/login";
    }

}
