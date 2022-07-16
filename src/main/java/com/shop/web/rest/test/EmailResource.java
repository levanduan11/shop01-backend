package com.shop.web.rest.test;

import com.shop.service.MailService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test/email")
public class EmailResource {

    private final MailService mailService;

    public EmailResource(MailService mailService) {
        this.mailService = mailService;
    }

    @GetMapping("")
    public String testSend(){
        String to="chokhacchoiem799@gmail.com";
        String content="please click this link ";
        String subject="spring boot";
        mailService.sendEmail(to,subject,content,false,false);
        return "sent success";
    }
}
