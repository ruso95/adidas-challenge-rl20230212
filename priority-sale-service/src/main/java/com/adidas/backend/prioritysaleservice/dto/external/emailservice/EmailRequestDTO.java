package com.adidas.backend.prioritysaleservice.dto.external.emailservice;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonSerialize
public class EmailRequestDTO {

    private String emailToAddress;
    private String subject;
    private String message;
    private String footer;
    private int notificationType;
}
