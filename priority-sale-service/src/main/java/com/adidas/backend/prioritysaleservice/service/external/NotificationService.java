package com.adidas.backend.prioritysaleservice.service.external;

import com.adidas.backend.prioritysaleservice.model.AdiClubSubscriptionMember;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

/**
 * <p>Notification service</p>
 *
 * @author <a href="mailto:andriyharyuk_95@hotmail.com">
 */
@Service
@Slf4j
@RequiredArgsConstructor
@EnableAsync
public class NotificationService {

    private final WebClient webClient;

    @Value("${email-service.uri}")
    private String emailServiceUri;
    @Value("${email-service.port}")
    private String emailServicePort;

    public void notifyWinner(AdiClubSubscriptionMember adiClubSubscriptionMember) {
        log.info("Sending notification to the winner {}", adiClubSubscriptionMember.getEmail());
        try {
            webClient.post()
                    .uri(getEmailServiceUri() + "/email/sale-winner",
                            uriBuilder -> uriBuilder.queryParam("emailWinner", adiClubSubscriptionMember.getEmail()).build())
                    .retrieve().bodyToFlux(Void.class).subscribe();
        } catch (Exception ex){
            log.error("Notification service error: {}", ex.getMessage(), ex);
            notifyWinnerDLQ(adiClubSubscriptionMember);
        }
    }

    public void notifyWinnerDLQ(AdiClubSubscriptionMember adiClubSubscriptionMember){
        log.warn("Notification service not available. Preparing MQ for {}", adiClubSubscriptionMember);
        // TODO future implementation: prepare notification DLQ
    }

    private String getEmailServiceUri() {
        return emailServiceUri + ":" + emailServicePort;
    }
}
