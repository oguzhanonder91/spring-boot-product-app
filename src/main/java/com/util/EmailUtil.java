package com.util;

import com.common.entity.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;

import javax.mail.internet.MimeMessage;
import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.Locale;

@Component
public class EmailUtil {

    private static final String USER = "user";

    private static final String URL = "url";

    private final Logger LOGGER = LoggerFactory.getLogger(EmailUtil.class);


    @Autowired
    private MessageSource messageSource;

    @Autowired
    private JavaMailSender javaMailSender;

    @Autowired
    private CommonUtil commonUtil;

    @Autowired
    private SpringTemplateEngine springTemplateEngine;

    public SimpleMailMessage constructResendRegistrationTokenEmail(Locale locale, String token, User user) {
        String url = commonUtil.getBaseMailUrl() + File.separator + "user" + File.separator + "registrationConfirm" + File.separator + token + File.separator + user.getId();
        String message = messageSource.getMessage("message.resendToken", null, locale);
        return constructEmail("Resend Registration Token", message + " \r\n" + url, user);
    }

    public SimpleMailMessage constructResetPasswordTokenEmail(Locale locale, String token, User user) {
        String url = commonUtil.getBaseMailUrl() + File.separator + "user" + File.separator + "changePassword" + File.separator + token + File.separator + user.getId();
        String message = messageSource.getMessage("message.resetPassword", null, locale);
        return constructEmail("Reset Password", message + " \r\n" + url, user);
    }

    public SimpleMailMessage constructRegistrationTokenEmail(Locale locale, String token, User user) {
        String url = commonUtil.getBaseMailUrl() + File.separator + "user" + File.separator + "registrationConfirm" + File.separator + token + File.separator + user.getId();
        String message = messageSource.getMessage("message.regSucc", null, locale);
        return constructEmail("Registration Confirmation", message + " \r\n" + url, user);
    }

    public SimpleMailMessage constructEmail(String subject, String body, User user) {
        SimpleMailMessage email = new SimpleMailMessage();
        email.setSubject(subject);
        email.setText(body);
        email.setTo(user.getEmail());
        email.setFrom(commonUtil.getSupportMailUrl());
        return email;
    }

    @Async
    public void sendActivationEmail(Locale locale, User user, String token) {
        String link = "user" + File.separator + "registrationConfirm" + File.separator + token + File.separator + user.getId();
        LOGGER.debug("Sending activation email to '{}'", user.getEmail());
        sendEmailFromTemplate(locale, user, "mail/activation", "email.activation.title", link);
    }

    @Async
    public void sendResetPasswordEmail(Locale locale, User user, String token) {
        String link =  "user" + File.separator + "changePassword" + File.separator + token + File.separator + user.getId();
        LOGGER.debug("Sending Reset Password email to '{}'", user.getEmail());
        sendEmailFromTemplate(locale, user, "mail/passwordReset", "message.resetPassword", link);
    }

    @Async
    public void sendEmailFromTemplate(Locale locale, User user, String templateName, String titleKey, String path) {
        Context context = new Context(locale);
        context.setVariable(USER, user);
        context.setVariable(URL, commonUtil.getBaseMailUrl() + File.separator + path);
        String content = springTemplateEngine.process(templateName, context);
        String subject = messageSource.getMessage(titleKey, null, locale);
        sendEmail(user.getEmail(), subject, content, false, true);

    }

    public void sendEmail(SimpleMailMessage simpleMailMessage) {
        javaMailSender.send(simpleMailMessage);
    }

    @Async
    public void sendEmail(String to, String subject, String content, boolean isMultipart, boolean isHtml) {
        LOGGER.debug("Send email[multipart '{}' and html '{}'] to '{}' with subject '{}' and content={}",
                isMultipart, isHtml, to, subject, content);

        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        try {
            MimeMessageHelper message = new MimeMessageHelper(mimeMessage, isMultipart, StandardCharsets.UTF_8.name());
            message.setTo(to);
            message.setFrom(commonUtil.getSupportMailUrl());
            message.setSubject(subject);
            message.setText(content, isHtml);
            javaMailSender.send(mimeMessage);
            LOGGER.debug("Sent email to User '{}'", to);
        } catch (Exception e) {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.warn("Email could not be sent to user '{}'", to, e);
            } else {
                LOGGER.warn("Email could not be sent to user '{}': {}", to, e.getMessage());
            }
        }
    }
}
