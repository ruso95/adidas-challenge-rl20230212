package com.adidas.backend.prioritysaleservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SubscriptionRequestDTO {

    private String email;
    private List<String> emailList;

}
