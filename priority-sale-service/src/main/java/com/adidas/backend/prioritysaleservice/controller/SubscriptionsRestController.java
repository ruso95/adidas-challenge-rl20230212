package com.adidas.backend.prioritysaleservice.controller;

import com.adidas.backend.prioritysaleservice.dto.SubscriptionRequestDTO;
import com.adidas.backend.prioritysaleservice.service.SubscriptionService;
import com.adidas.backend.prioritysaleservice.util.exception.ServiceException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/subscriptions/")
@RequiredArgsConstructor
@Slf4j
public class SubscriptionsRestController {

    private final SubscriptionService subscriptionService;

    @PostMapping("new")
    public ResponseEntity<String> newSubscription(@RequestBody SubscriptionRequestDTO subscriptionRequest) {
        log.info("New subscription request registered: " + subscriptionRequest);
        try {
            subscriptionService.newSubscription(subscriptionRequest);
            return ResponseEntity.ok("Subscription completed successfully");
        } catch (ServiceException e) {
            String msgError = String.format("One or more services are down: %s", e.getMessage());
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(msgError);
        } catch (Exception e) {
            String msgError = String.format("Error while processing the new subscription: %s", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(msgError);
        }
    }
}
