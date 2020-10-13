package com.common.controller;

import com.common.dao.MenuDao;
import com.common.dao.PermissionDao;
import com.common.dao.TokenDao;
import com.common.dao.UserDao;
import com.common.dto.UserDto;
import com.common.entity.*;
import com.util.CommonUtil;
import com.util.EmailUtil;
import com.util.SecurityUtil;
import com.util.annotations.MyServiceAnnotation;
import com.util.annotations.MyServiceGroupAnnotation;
import com.util.enums.MethodType;
import com.util.enums.PermissionType;
import com.util.enums.TokenType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.Calendar;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("user")
@MyServiceGroupAnnotation(name = "Kullanıcı İşlemleri", path = "user")
public class UserController {

    @Autowired
    private UserDao userDao;

    @Autowired
    private TokenDao tokenDao;

    @Autowired
    private EmailUtil emailUtil;

    @Autowired
    private MessageSource messageSource;

    @Autowired
    private PermissionDao permissionDao;

    @Autowired
    private SecurityUtil securityUtil;

    @Autowired
    private MenuDao menuDao;


    @PostMapping(path = "/registration")
    @MyServiceAnnotation(name = "Kullanıcı Kayıt", path = "/registration", type = MethodType.POST, permissionRoles = {"ADMIN", "USER"})
    public ResponseEntity<String> registrationNewUser(@Valid @RequestBody final UserDto userDto, HttpServletRequest httpServletRequest) {
        final User user = userDao.registerNewUserAccount(userDto);
        userDao.save(user);
        final Token token = tokenDao.prepareRegistrationAndPassword(user.getEmail(), TokenType.REGISTRATION, httpServletRequest);
        tokenDao.create(token);
        final SimpleMailMessage mailMessage = emailUtil.constructRegistrationTokenEmail(CommonUtil.getAppUrl(httpServletRequest), httpServletRequest.getLocale(), token.getValue(), user);
        emailUtil.sendEmail(mailMessage);
        String message = messageSource.getMessage("message.regSuccAndEmail", null, httpServletRequest.getLocale());
        return new ResponseEntity<>(message, HttpStatus.OK);
    }

    @GetMapping("/registrationConfirm/{paramToken}")
    @MyServiceAnnotation(name = "Kullanıcı Kayıt Doğrulama", path = "/registrationConfirm/{}", type = MethodType.GET, permissionRoles = {"ADMIN", "USER"})
    public ResponseEntity<String> confirmRegistration(HttpServletRequest httpServletRequest, @PathVariable String paramToken) {
        Token token = tokenDao.controlTokenRegistrationAndPassword(paramToken, TokenType.REGISTRATION, httpServletRequest);
        if (token == null) {
            return new ResponseEntity<>(messageSource.getMessage("auth.message.invalidToken", null, httpServletRequest.getLocale()), HttpStatus.OK);
        }

        Calendar cal = Calendar.getInstance();
        if ((token.getExpiry() - cal.getTimeInMillis()) <= 0) {
            tokenDao.deleteRealToken(token);
            return new ResponseEntity<>(messageSource.getMessage("auth.message.expired", null, httpServletRequest.getLocale()), HttpStatus.OK);
        }

        User user = userDao.findByEmail(token.getEmail());
        user.setEnabled(true);
        userDao.update(user);
        return new ResponseEntity<>(messageSource.getMessage("message.accountVerified", null, httpServletRequest.getLocale()), HttpStatus.OK);
    }

    @GetMapping(path = "/menus")
    @MyServiceAnnotation(name = "Kullanıcı Menülerini Listele", path = "/menus", type = MethodType.GET, permissionRoles = {"ADMIN", "USER"})
    public ResponseEntity<User> getUserAndMenus() {
        User user = userDao.findByEmail(securityUtil.getCurrentAuditor());
        List<Permission> permissions = permissionDao.findByTypeAndRolesIn(PermissionType.MENU, (List<Role>) user.getRoles());
        List<String> menuIds = permissions.stream()
                .map(p -> p.getItemId())
                .collect(Collectors.toList());
        List<Menu> menus = menuDao.getMenusOfUser(menuIds);
        user.setMenus(menus);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }
}
