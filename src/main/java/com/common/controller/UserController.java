package com.common.controller;

import com.common.dao.TokenDao;
import com.common.dao.UserDao;
import com.common.dto.UserDto;
import com.common.entity.Token;
import com.common.entity.User;
import com.util.CommonUtil;
import com.util.EmailUtil;
import com.util.enums.TokenType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.Calendar;

@RestController
@RequestMapping("user")
public class UserController {

    @Autowired
    private UserDao userDao;

    @Autowired
    private TokenDao tokenDao;

    @Autowired
    private EmailUtil emailUtil;

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private MessageSource messages;

    @PostMapping(path = "/registration")
    public ResponseEntity<String> registrationNewUser(@Valid @RequestBody final UserDto userDto, HttpServletRequest httpServletRequest) {
        final User user = userDao.registerNewUserAccount(userDto);
        userDao.save(user);
        final Token token = tokenDao.prepareRegistrationAndPassword(user.getEmail(), TokenType.REGISTRATION, httpServletRequest);
        tokenDao.create(token);
        final SimpleMailMessage mailMessage = emailUtil.constructRegistrationTokenEmail(CommonUtil.getAppUrl(httpServletRequest), httpServletRequest.getLocale(), token.getValue(), user);
        mailSender.send(mailMessage);
        String message = messages.getMessage("message.regSuccAndEmail", null, httpServletRequest.getLocale());
        return new ResponseEntity<>(message, HttpStatus.OK);
    }

    @GetMapping("/registrationConfirm/{paramToken}")
    public ResponseEntity<String> confirmRegistration(HttpServletRequest httpServletRequest, @PathVariable String paramToken) {
        Token token = tokenDao.controlTokenRegistrationAndPassword(paramToken, TokenType.REGISTRATION, httpServletRequest);
        if (token == null) {
            return new ResponseEntity<>(messages.getMessage("auth.message.invalidToken", null, httpServletRequest.getLocale()), HttpStatus.OK);
        }

        Calendar cal = Calendar.getInstance();
        if ((token.getExpiry() - cal.getTimeInMillis()) <= 0) {
            tokenDao.deleteRealToken(token);
            return new ResponseEntity<>(messages.getMessage("auth.message.expired", null, httpServletRequest.getLocale()), HttpStatus.OK);
        }

        User user = userDao.findByEmail(token.getEmail());
        user.setEnabled(true);
        userDao.update(user);
        return new ResponseEntity<>(messages.getMessage("message.accountVerified", null, httpServletRequest.getLocale()), HttpStatus.OK);
    }
}
