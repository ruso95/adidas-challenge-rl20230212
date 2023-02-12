package com.adidas.backend.emailservice.model.factory;

import com.adidas.backend.emailservice.dto.EmailRequestDTO;
import com.adidas.backend.emailservice.model.EmailModel;

public class EmailModelFactory {

    public static EmailModel fromRequest(EmailRequestDTO emailRequestDTO) {
        return EmailModel.builder()
                .mailTo(emailRequestDTO.getEmailToAddress())
                .subject(emailRequestDTO.getSubject())
                .message(emailRequestDTO.getMessage())
                .footer(emailRequestDTO.getFooter())
                .type(emailRequestDTO.getNotificationType())
                .build();
    }
}
