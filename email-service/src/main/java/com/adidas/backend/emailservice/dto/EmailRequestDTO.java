package com.adidas.backend.emailservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EmailRequestDTO {

    private String emailToAddress;
    private String subject;
    private String message;
    private String footer;
    private int notificationType;
}
