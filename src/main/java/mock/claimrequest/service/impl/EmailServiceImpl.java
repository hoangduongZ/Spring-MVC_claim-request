package mock.claimrequest.service.impl;

import mock.claimrequest.dto.claim.ClaimEmailRequestDTO;
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

@Service
public class EmailServiceImpl implements EmailService {
    @Autowired
    private JavaMailSender emailSender;

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
        message.setTo("worksviethoang@gmail.com");
        message.setCc(emailRequest.getEmailCC());
        message.setSubject(subject);
        message.setText(body);

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
