package com.adidas.backend.emailservice.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EmailModel {

    private String mailTo;
    private String subject;
    private String message;
    private String footer;
    private int type;
    private int failCount = 0;

    public void increaseFailCount() {
        failCount++;
    }
}
