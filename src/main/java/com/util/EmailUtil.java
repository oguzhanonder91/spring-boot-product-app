package com.util;

import com.common.configuration.CaboryaConfig;
import com.common.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.Locale;

@Component
public class EmailUtil {

    @Autowired
    private MessageSource messageSource;

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private CaboryaConfig caboryaConfig;

    public static final int TOKEN_REGISTRATION_AND_RESET_PASSWORD_EXPIRATION = 60 * 24;

    public SimpleMailMessage constructResendRegistrationTokenEmail(String contextPath, Locale locale, String token, User user) {
        String url = contextPath + File.separator + "user" + File.separator + "registrationConfirm" + File.separator + token;
        String message = messageSource.getMessage("message.resendToken", null, locale);
        return constructEmail("Resend Registration Token", message + " \r\n" + url, user);
    }

    public SimpleMailMessage constructResetPasswordTokenEmail(String contextPath, Locale locale, String token, User user) {
        String url = contextPath + File.separator + "user" + File.separator + "changePassword" + File.separator + user.getId() + File.separator + token;
        String message = messageSource.getMessage("message.resetPassword", null, locale);
        return constructEmail("Reset Password", message + " \r\n" + url, user);
    }

    public SimpleMailMessage constructRegistrationTokenEmail(String contextPath, Locale locale, String token, User user) {
        String url = caboryaConfig.getSupport().getBaseUrl() + File.separator + "user" + File.separator + "registrationConfirm" + File.separator + token;
        String message = messageSource.getMessage("message.regSucc", null, locale);
        return constructEmail("Registration Confirmation", message + " \r\n" + url, user);
    }

    public SimpleMailMessage constructEmail(String subject, String body, User user) {
        SimpleMailMessage email = new SimpleMailMessage();
        email.setSubject(subject);
        email.setText(body);
        email.setTo(user.getEmail());
        email.setFrom(caboryaConfig.getSupport().getMail());
        return email;
    }

    public void sendEmail(SimpleMailMessage simpleMailMessage) {
        mailSender.send(simpleMailMessage);
    }
}
