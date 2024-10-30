package mock.claimrequest.service.impl;

import jakarta.servlet.http.HttpServletRequest;
import mock.claimrequest.dto.claim.ClaimEmailRequestDTO;
import mock.claimrequest.security.TokenCache;
import mock.claimrequest.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
public class EmailServiceImpl implements EmailService {
    private final JavaMailSender emailSender;
    private final TokenCache tokenCache;

    public EmailServiceImpl(JavaMailSender emailSender, TokenCache tokenCache) {
        this.emailSender = emailSender;
        this.tokenCache = tokenCache;
    }

    @Override
    public void sendClaimRequestEmail(ClaimEmailRequestDTO emailRequest) throws IOException {
        String emailTemplate = loadEmailTemplate();

        String subject = String.format("Claim Request %s %s - %s",
                emailRequest.getProjectName(),
                emailRequest.getStaffName(),
                emailRequest.getStaffId());

        String body = replacePlaceholders(
                emailTemplate,
                emailRequest.getPmName(),
                emailRequest.getProjectName(),
                emailRequest.getStaffName(),
                emailRequest.getStaffId(),
                emailRequest.getClaimLink()
        );

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(emailRequest.getEmailPm());
        message.setCc(emailRequest.getEmailCC());
        message.setSubject(subject);
        message.setText(body);

        emailSender.send(message);
    }

    public String generateResetLink(HttpServletRequest request, String token) {
        String baseUrl = request.getRequestURL().toString().replace(request.getRequestURI(), "");
        String resetLink = baseUrl + "/password/reset?token=" + token;
        return resetLink;
    }

    public void sendResetLink(HttpServletRequest request,String email) {
        String token = UUID.randomUUID().toString();
        tokenCache.put(token, email);

        String resetLink = generateResetLink(request, token);
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject("Reset Your Password");
        message.setText("To reset your password, please click the link below:\n" + resetLink);

        emailSender.send(message);

    }

    private String loadEmailTemplate() throws IOException {
        StringBuilder template = new StringBuilder();
        try (InputStream inputStream = getClass().getResourceAsStream("/mail_template/ET1.txt");
             BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
            String line;
            while ((line = reader.readLine()) != null) {
                template.append(line).append("\n");
            }
        }
        return template.toString();
    }

    private String replacePlaceholders(String template, String pmName, String projectName, String staffName, String staffId, String link) {
        return template
                .replace("${pmName}", pmName)
                .replace("${projectName}", projectName)
                .replace("${staffName}", staffName)
                .replace("${staffId}", staffId)
                .replace("${link}", link);
    }

}
