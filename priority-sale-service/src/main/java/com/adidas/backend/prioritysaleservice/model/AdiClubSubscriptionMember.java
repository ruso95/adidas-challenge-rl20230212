package com.adidas.backend.prioritysaleservice.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.Comparator;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AdiClubSubscriptionMember implements Comparable<AdiClubSubscriptionMember> {

    private String email;
    private Integer points;
    private Instant registrationDate;

    @Override
    public int compareTo(AdiClubSubscriptionMember member) {
        int pointsDif = this.points.compareTo(member.points);
        if (pointsDif != 0) {
            return pointsDif;
        }
        // older registrations goes first
        Comparator<Instant> sortByRegistration = Instant::compareTo;
        return sortByRegistration.reversed().compare(this.registrationDate, member.registrationDate);
    }
}
