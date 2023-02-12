package com.adidas.backend.emailservice.service;

import com.adidas.backend.emailservice.cmd.MailSenderCmd;
import com.adidas.backend.emailservice.model.EmailModel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
@Slf4j
public class EmailService {
    private static final int DEFAULT_MAX_FAILED_EMAIL_RETRIES = 3;

    @Autowired
    private MailSenderCmd cmd;
    @Value("${emailRetryEnabled}")
    private Integer retryEnabled;
    @Value("${maxEmailRetries}")
    private Integer maxEmailRetries;

    public void sendSaleWinnerMail(EmailModel email) {
        log.info("Sending winning mail to {}", email.getMailTo());
        email.setSubject("AdidasChallengeMail - Subscription Sales ");
        email.setMessage("Congratulations you are the winner for the sale! \n Thank you for your support and he hope to see you soon!");
        email.setFooter("-- This is a test!! Not real sale was going on --\nIf you received this, please delete or ignore!");

        cmd.sendMail(email);
    }

    public void simpleNotification(EmailModel email) {
        log.info("Sending simple mail to {}", email.getMailTo());
        cmd.sendMail(email);
    }

    @Scheduled(cron = "${retry-failed-mails.cron}")
    public void retryFailedMail() {
        if (!isRetryEnabled()) {
            return;
        }
        List<EmailModel> failedEmailList = getFailedEmailList();
        log.debug("Failed email list ready retry ({}): {}", failedEmailList.size(), failedEmailList);

        for (EmailModel failedEmail : failedEmailList) {
            if (failedEmail.getFailCount() >= getMaxEmailRetries()) {
                log.error("After {} tries max retry reached by email {} ", failedEmail, getMaxEmailRetries());
                discardEmailForMaxAttempsFailed(failedEmail);
                createAlert();
                continue;
            }
            log.info("Retrying email {}", failedEmail);
            cmd.sendMail(failedEmail);
        }
    }

    private void createAlert() {
        // TODO future implementation: create alert system
    }

    private void discardEmailForMaxAttempsFailed(EmailModel failedEmail) {
        // TODO future implementation: discard max emails retry
    }

    private List<EmailModel> getFailedEmailList() {
        // TODO future implementation: retrieve failed emails from DB
        return Collections.emptyList();
    }

    private int getMaxEmailRetries() {
        if (maxEmailRetries == null) {
            log.warn("[CONFIGURATION CHECK REQUIRED] maxEmailRetries not properly set for email-service. Default is used {}", DEFAULT_MAX_FAILED_EMAIL_RETRIES);
            return DEFAULT_MAX_FAILED_EMAIL_RETRIES;
        }
        return maxEmailRetries;
    }

    private boolean isRetryEnabled() {
        if (this.retryEnabled == null) {
            log.warn("[CONFIGURATION CHECK REQUIRED] retryEnabled not properly set for email-service. False is returned");
            return false;
        }
        return retryEnabled == 1;
    }
}
