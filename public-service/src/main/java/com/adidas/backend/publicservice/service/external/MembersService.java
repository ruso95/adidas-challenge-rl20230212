package com.adidas.backend.publicservice.service.external;

import com.adidas.backend.publicservice.dto.external.SubscriptionRequestDTO;
import com.adidas.backend.publicservice.util.exceptions.BadConfigException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

@Service
@RequiredArgsConstructor
@Slf4j
public class MembersService {

    private final WebClient.Builder webClientBuilder;

    @Value("${priority-sale-service.uri}")
    private String membersServiceUri;
    @Value("${priority-sale-service.port}")
    private String membersServicePort;

    public ResponseEntity<String> newSubscription(SubscriptionRequestDTO subscriptionRequestDTO) throws BadConfigException {
        return webClientBuilder.build().post()
                .uri(getMembersServiceUri() + "/subscriptions/new")
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(subscriptionRequestDTO))
                .retrieve().toEntity(String.class).block();
    }

    /**
     * @return uri:port
     * @throws BadConfigException if uri or port is null or blank
     */
    private String getMembersServiceUri() throws BadConfigException {
        if (StringUtils.isBlank(membersServiceUri) || StringUtils.isBlank(membersServicePort)) {
            log.error("[CONFIGURATION CHECK REQUIRED] The priority service URI is not properly set. Please review the configurations");
            throw new BadConfigException("[BAD-CONFIGURATION] Please check configurations");
        }
        return membersServiceUri + ":" + membersServicePort;
    }
}
