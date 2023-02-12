package com.adidas.backend.prioritysaleservice.model.factory;

import com.adidas.backend.prioritysaleservice.dto.external.adiclub.AdiClubMemberInfoDto;
import com.adidas.backend.prioritysaleservice.dto.external.emailservice.EmailRequestDTO;
import com.adidas.backend.prioritysaleservice.model.AdiClubSubscriptionMember;

public class AdiClubSubscriptionFactory {
    private AdiClubSubscriptionFactory() {
        throw new IllegalStateException("Utility class");
    }

    public static AdiClubSubscriptionMember get(AdiClubMemberInfoDto dto) {
        return AdiClubSubscriptionMember.builder()
                .points(dto.getPoints())
                .email(dto.getEmail())
                .registrationDate(dto.getRegistrationDate())
                .build();
    }

    public static EmailRequestDTO toEmailService(AdiClubSubscriptionMember dto) {
        return EmailRequestDTO.builder()
                .emailToAddress(dto.getEmail())
                .build();
    }
}
