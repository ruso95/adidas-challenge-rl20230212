package com.adidas.backend.publicservice.controller;

import com.adidas.backend.publicservice.dto.external.SubscriptionRequestDTO;
import com.adidas.backend.publicservice.service.external.MembersService;
import com.adidas.backend.publicservice.util.enums.CustomCircuitBreakerEnum;
import com.adidas.backend.publicservice.util.exceptions.BadConfigException;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>Controller for the subscription service</p>
 * <p>The URI are configured via docker-compose</p>
 * <p>CircuitBreaker is implemented for this controller: it will be open for 10s after 50% threshold is reached in 5 calls (3 when half_open)</p>
 *
 * @author <a href="mailto:andriyharyuk_95@hotmail.com">
 */
@RestController
@RequestMapping(value = "/members-subscription")
@RequiredArgsConstructor
@Slf4j
public class MemberSubscriptionRestController {

    private final MembersService membersService;


    /**
     * <p>Create new single subscription</p>
     * <p>In case of service not available SERVICE_UNAVAILABLE will be returned</p>
     * <p>Circuit breaker implemented for this method</p>
     *
     * @param newEmailSubscription the user mail
     * @return response return by member-service if OK. INTERNAL_SERVER_ERROR if bad configurations
     * @see #fallbackSubscriptionMethod
     */
    @PostMapping()
    @CircuitBreaker(name = CustomCircuitBreakerEnum.Constants.MEMBERS_SERVICE, fallbackMethod = "fallbackSubscriptionMethod")
    public ResponseEntity<String> newSubscription(@RequestParam("emailAddress") String newEmailSubscription) {
        log.info("New subscription requested for {}", newEmailSubscription);
        try {
            ResponseEntity<String> response = membersService.newSubscription(SubscriptionRequestDTO.builder().email(newEmailSubscription).build());

            if (response == null) {
                log.error("[NEW SUBSCRIPTION] Response from priority service is null");
                return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body("Oops! Seems like we're having some problems in the service! We gonna try to fix it as soon as possible, please try again later!");
            }

            log.debug("Response from priority service {}: {}", response.getStatusCode(), response.getBody());
            return response;
        } catch (BadConfigException badConfigException){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(badConfigException.getMessage());
        }
    }

    /**
     * <p>Create single or multiple subscriptions in one request, not exclusive.</p>
     * <p>In case of service not available SERVICE_UNAVAILABLE will be returned</p>
     * <p>Circuit breaker implemented for this method</p>
     *
     * @param subscriptionRequestDTO contains the user mail and/or the users email address for the subscription
     * @return response return by member-service if OK. INTERNAL_SERVER_ERROR if bad configurations
     * @see #fallbackSubscriptionMethod
     */
    @PostMapping("/batch")
    @CircuitBreaker(name = CustomCircuitBreakerEnum.Constants.MEMBERS_SERVICE, fallbackMethod = "fallbackSubscriptionMethod")
    public ResponseEntity<String> newSubscription(@RequestBody SubscriptionRequestDTO subscriptionRequestDTO) {
        log.info("New subscription requested for {}", subscriptionRequestDTO);
        try {
            ResponseEntity<String> response = membersService.newSubscription(subscriptionRequestDTO);

            if (response == null) {
                log.error("[NEW SUBSCRIPTION] Response from priority service is null");
                return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body("Oops! Seems like we're having some problems in the service! We gonna try to fix it as soon as possible, please try again later!");
            }

            log.debug("Response from priority service {}: {}", response.getStatusCode(), response.getBody());
            return response;
        } catch (BadConfigException badConfigException){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(badConfigException.getMessage());
        }
    }

    /**
     * <p>Breaker fallback method for the new subscriptions methods</p>
     * <p>SERVICE_UNAVAILABLE will be return with a custom message, the exception will be logged in ERROR level</p>
     *
     * @param ex the exception passed by the breaker
     * @return HttpStatus.SERVICE_UNAVAILABLE with String body
     */
    public ResponseEntity<String> fallbackSubscriptionMethod(Exception ex){
        log.error("Fallback Subscription Method. Original error {}", ex.getMessage(), ex);
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body("The subscription service is currently not available. Please try again later.");
    }

}
