package com.adidas.backend.prioritysaleservice.service;

import com.adidas.backend.prioritysaleservice.dto.SubscriptionRequestDTO;
import com.adidas.backend.prioritysaleservice.dto.external.adiclub.AdiClubMemberInfoDto;
import com.adidas.backend.prioritysaleservice.model.AdiClubSubscriptionMember;
import com.adidas.backend.prioritysaleservice.model.factory.AdiClubSubscriptionFactory;
import com.adidas.backend.prioritysaleservice.service.external.NotificationService;
import com.adidas.backend.prioritysaleservice.service.external.PersistenceService;
import com.adidas.backend.prioritysaleservice.util.exception.ServiceException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;

/**
 * <p>Service for the subscriptions</p>
 * <p>The URI for adi-club are configured via docker-compose</p>
 * <p>It has a scheduled method for that picks a winner for the sale between all subscriptions and request a notification</p>
 *
 * @author <a href="mailto:andriyharyuk_95@hotmail.com">
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class SubscriptionService {

    private final PersistenceService persistenceService;
    private final NotificationService notificationService;
    private final WebClient webClient;

    @Value("${adiclub-service.uri}")
    private String adiClubServiceUri;
    @Value("${adiclub-service.port}")
    private String adiClubServicePort;

    /**
     * <p>Request the member info to adi-club and adds him to the subs queue waiting to be picked by {@link #pickWinner()}</p>
     * @param subscriptionRequestDTO contains the email(s) of the subscribers
     * @throws ServiceException in case of adi-club service fail
     */
    public void newSubscription(SubscriptionRequestDTO subscriptionRequestDTO) throws ServiceException {
        List<String> emailList = subscriptionRequestDTO.getEmailList();
        if (!CollectionUtils.isEmpty(emailList)) {
            log.debug("Multiple subscriptions from one request {}", emailList.size());
            for (String email : emailList) {
                addSubscription(email);
            }
        }
        if (StringUtils.isNotBlank(subscriptionRequestDTO.getEmail())) {
            log.debug("Simple subscription request {}", subscriptionRequestDTO.getEmail());
            addSubscription(subscriptionRequestDTO.getEmail());
        }
        log.info("Subscriptions requests processed");
    }

    /**
     * <p>Retrieves the member info to adi-club and adds him to the queue</p>
     *
     * @param email member email to check and add
     * @throws ServiceException in case of adi-club service fail
     */
    protected void addSubscription(String email) throws ServiceException {
        AdiClubMemberInfoDto result = getAdiClubMemberInfo(email);
        if (result != null) {
            log.info("Added subscription {}", result);
            persistenceService.add(AdiClubSubscriptionFactory.get(result));
        }
    }

    /**
     * <p>Request the member info to the adi-club service</p>
     * <p>Uri and port values are docker defined</p>
     *
     * @param email member email to search
     * @return member info with email, points and creation date
     * @throws ServiceException in case of adi-club service fail for any reason
     */
    protected AdiClubMemberInfoDto getAdiClubMemberInfo(String email) throws ServiceException {
        log.info("Requesting membership information for {}", email);
        try {
            AdiClubMemberInfoDto result = webClient.get()
                    .uri(getAdiClubServiceUri()+ "/adiclub", uriBuilder -> uriBuilder.queryParam("emailAddress", email).build())
                    .retrieve().bodyToMono(AdiClubMemberInfoDto.class).block();
            log.debug("AdiClubMemberInfoDto {}", result);
            return result;
        } catch (Exception e) {
            String msgError = String.format("[adiclub-service] Error while retrieving information from user %s", email);
            log.error(msgError, e);
            throw new ServiceException(msgError, e);
        }
    }

    private String getAdiClubServiceUri() {
        return adiClubServiceUri + ":" + adiClubServicePort;
    }

    /**
     * <p>Picks from the queue for the sale and notifies him about it</p>
     * <p>The criteria is:</p>
     * <ol>
     *     <li>Highest points</li>
     *     <li>Oldest member</li>
     * </ol>
     */
    @Async
    @Scheduled(cron = "${winner-wheel.cron}")
    public void pickWinner() {
        log.trace("[SCHEDULED] Executing pickWinner method");
        AdiClubSubscriptionMember winner = persistenceService.getTopSubscriber();
        if (winner == null) {
            log.trace("[SCHEDULED] No subscribers available for the current sale");
            return;
        }
        log.info("[SCHEDULED] Winner picked {}. Starting notification", winner);
        notificationService.notifyWinner(winner);
    }
}
