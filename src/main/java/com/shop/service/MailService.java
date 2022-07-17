package com.shop.service;

import com.shop.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;
import java.nio.charset.StandardCharsets;
import java.util.Locale;

@Service
public class MailService {

    private final Logger log = LoggerFactory.getLogger(MailService.class);

    private static final String USER = "user";

    private static final String BASE_URL = "baseUrl";
    //private static final String CLIENT_URL="http://localhost:4200";
    private static final String CLIENT_URL="https://duan-shop.netlify.app";
    private final JavaMailSender javaMailSender;
    private final MessageSource messageSource;
    private final SpringTemplateEngine templateEngine;


    public MailService(JavaMailSender javaMailSender, MessageSource messageSource, SpringTemplateEngine templateEngine) {
        this.javaMailSender = javaMailSender;
        this.messageSource = messageSource;
        this.templateEngine = templateEngine;
    }

    @Async
    public void sendEmail(String to, String subject, String content, boolean isMultipart, boolean isHtml) {

        log.debug("send email[multipart '{}' and html '{}' to '{}' with subject '{}' and content={} ]",
                isMultipart,
                isHtml,
                to, subject,
                content);
        MimeMessage message = javaMailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, isMultipart, StandardCharsets.UTF_8.name());
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(content, isHtml);
            javaMailSender.send(message);
            log.debug("sent email to user '{}'", to);
        } catch (MessagingException e) {
            log.warn("email could not be sent to user '{}'", to, e);
        }
    }

    @Async
    public void sendEmailFromTemplate(User user, String templateName, String titleKey) {
        if (user.getEmail() == null) {
            log.debug("Email doesn't exist for user '{}'", user.getUsername());
            return;
        }

        Locale locale = Locale.forLanguageTag("en-US");
        Context context = new Context(locale);
        context.setVariable(USER, user);
        context.setVariable(BASE_URL, CLIENT_URL);
        String content = templateEngine.process(templateName, context);
        String subject = messageSource.getMessage(titleKey, null, locale);
        sendEmail(user.getEmail(), subject, content, false, true);
    }

    @Async
    public void sendActivationEmail(User user) {
        log.debug("Sending activation email to '{}'", user.getEmail());
        sendEmailFromTemplate(user, "mail/activationEmail", "email.activation.title");

    }

    @Async
    public void sendCreationEmail(User user) {
        log.debug("Sending creation email to '{}'", user.getEmail());
        sendEmailFromTemplate(user, "mail/creationEmail", "email.activation.title");

    }

    @Async
    public void sendPasswordResetEmail(User user) {
        log.debug("Sending password reset email to '{}'", user.getEmail());
        sendEmailFromTemplate(user, "mail/passwordResetEmail", "email.reset.title");

    }
}
