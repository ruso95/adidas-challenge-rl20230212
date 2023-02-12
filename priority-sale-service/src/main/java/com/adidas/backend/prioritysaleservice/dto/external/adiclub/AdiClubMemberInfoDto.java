package com.adidas.backend.prioritysaleservice.dto.external.adiclub;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

/**
 * AdiClumbMemberInfoDTO from adiclub-service/getAdiClubMemberInfo
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AdiClubMemberInfoDto {
    private String email;
    private Integer points;
    private Instant registrationDate;
}
