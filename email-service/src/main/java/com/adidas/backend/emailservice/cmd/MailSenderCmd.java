package com.adidas.backend.emailservice.cmd;

import com.adidas.backend.emailservice.model.EmailModel;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class MailSenderCmd {
    private static final String DEFAULT_FROM_EMAIL = "this_would_be_default_fallback_email@test.com";

    private final JavaMailSender mailSender;

    @Autowired
    public MailSenderCmd(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    @Value("${fromEmail}")
    private String fromEmail;

    public void sendMail(EmailModel email) {
        if (StringUtils.isBlank(email.getMailTo()) || StringUtils.isBlank(email.getSubject())) {
            log.warn("Can't send email because either mailTo or subject was empty or null");
            return;
        }

        log.debug("Preparing to send mail: {}", email);
        try {
            // to include attachments MimeMessageHelper will be needed
            SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
            simpleMailMessage.setFrom(getFromEmail());
            simpleMailMessage.setTo(email.getMailTo());
            simpleMailMessage.setSubject(email.getSubject());

            String message = email.getMessage();
            if (StringUtils.isNotBlank(email.getFooter())) {
                message += "\n\n";
                message += email.getFooter();
            }
            simpleMailMessage.setText(message);

            mailSender.send(simpleMailMessage);
        } catch (Exception e) {
            log.warn("Failed to send email {} reason: {}", email, e);
            email.increaseFailCount();
            // TODO future implementation: add failed email to retry list
        }
    }

    private String getFromEmail() {
        if (StringUtils.isBlank(fromEmail)) {
            log.warn("[CONFIGURATION CHECK REQUIRED] fromEmail not properly set for email-service. Default is used {}", DEFAULT_FROM_EMAIL);
            return DEFAULT_FROM_EMAIL;
        }
        return fromEmail;
    }
}
