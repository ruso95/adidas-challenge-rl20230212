package com.adidas.backend.publicservice.dto.external;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


/**
 * <p>External DTO from the members-service for new subscriptions requests</p>
 *
 * @author <a href="mailto:andreyharyuk_95@hotmail.com">
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonSerialize
public class SubscriptionRequestDTO {

    private String email;
    private List<String> emailList;

}
