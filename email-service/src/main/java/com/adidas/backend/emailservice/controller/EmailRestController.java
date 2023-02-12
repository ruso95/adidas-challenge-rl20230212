package com.adidas.backend.emailservice.controller;

import com.adidas.backend.emailservice.dto.EmailRequestDTO;
import com.adidas.backend.emailservice.model.EmailModel;
import com.adidas.backend.emailservice.model.factory.EmailModelFactory;
import com.adidas.backend.emailservice.service.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/email/")
@RequiredArgsConstructor
public class EmailRestController {

    private final EmailService emailService;

    @PostMapping("sale-winner")
    @ResponseStatus(HttpStatus.OK)
    public void sendSaleWinnerMail(@RequestParam("emailWinner") String email) {
        emailService.sendSaleWinnerMail(EmailModel.builder().mailTo(email).build());
    }

    @PostMapping("notification")
    @ResponseStatus(HttpStatus.OK)
    public void simpleNotification(@RequestBody EmailRequestDTO notification) {
        emailService.simpleNotification(EmailModelFactory.fromRequest(notification));
    }
}
