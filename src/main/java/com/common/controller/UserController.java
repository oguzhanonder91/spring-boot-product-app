package com.common.controller;

import com.common.dao.MenuDao;
import com.common.dao.PermissionDao;
import com.common.dao.TokenDao;
import com.common.dao.UserDao;
import com.common.dto.BaseResponse;
import com.common.dto.PasswordDto;
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
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;
import java.util.Objects;
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
        emailUtil.sendActivationEmail(httpServletRequest.getLocale(), user, token.getValue());
        String message = messageSource.getMessage("message.regSuccAndEmail", null, httpServletRequest.getLocale());
        return new ResponseEntity<>(message, HttpStatus.OK);
    }

    @GetMapping("/registrationConfirm/{paramToken}/{userId}")
    @MyServiceAnnotation(name = "Kullanıcı Kayıt Doğrulama", path = "/registrationConfirm/{}/{}", type = MethodType.GET, permissionRoles = {"ADMIN", "USER"})
    public ResponseEntity<BaseResponse<String>> confirmRegistration(HttpServletRequest httpServletRequest, @PathVariable String paramToken, @PathVariable String userId) {
        Token token = tokenDao.controlTokenRegistrationAndPassword(paramToken, TokenType.REGISTRATION, httpServletRequest);
        ResponseEntity<BaseResponse<String>> responseEntity = tokenDao.controlTokenReturnBaseResponse(token, httpServletRequest, messageSource, userId,
                "auth.message.invalidToken",
                "auth.message.expired",
                "message.accountVerified");
        if (!StringUtils.isEmpty(Objects.requireNonNull(responseEntity.getBody()).getKey()) && responseEntity.getBody().getKey().equalsIgnoreCase(CommonUtil.TOKEN_VALID)) {
            User user = userDao.findByEmail(token.getEmail());
            user.setEnabled(true);
            userDao.update(user);
        }
        return responseEntity;
    }


    @GetMapping("/changePassword/{paramToken}/{userId}")
    @MyServiceAnnotation(name = "Kullanıcı Şifre Değiştirme", path = "/changePassword/{}/{}", type = MethodType.GET, permissionRoles = {"ADMIN", "USER"})
    public ResponseEntity<?> changePasswordControl(HttpServletRequest httpServletRequest, @PathVariable String paramToken, @PathVariable String userId) {
        Token token = tokenDao.controlTokenRegistrationAndPassword(paramToken, TokenType.CHANGE_PASSWORD, httpServletRequest);
        return tokenDao.controlTokenReturnBaseResponse(token, httpServletRequest, messageSource, userId,
                "auth.message.invalidToken",
                "token.changePassword.message.expired",
                "token.changePassword.message.valid");
    }


    @PutMapping(value = "/forgotPassword")
    @MyServiceAnnotation(name = "Kullanıcı Şifre Unuttum", path = "/resetPassword", type = MethodType.PUT, permissionRoles = {"ADMIN", "USER"})
    public ResponseEntity<String> resetPassword(HttpServletRequest request, @RequestBody final String userEmail) {
        User user = userDao.findByEmail(userEmail);
        if (user.isEnabled()) {
            Token token = tokenDao.prepareRegistrationAndPassword(user.getEmail(), TokenType.CHANGE_PASSWORD, request);
            tokenDao.create(token);
            emailUtil.sendResetPasswordEmail(request.getLocale(), user, token.getValue());
            return new ResponseEntity<>(messageSource.getMessage("message.resetPasswordEmail", null, request.getLocale()), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(messageSource.getMessage("auth.message.disabled", null, request.getLocale()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping(value = "/changePassword/{userId}")
    @MyServiceAnnotation(name = "Kullanıcı Şifre Değiştirme", path = "/changePassword", type = MethodType.PUT, permissionRoles = {"ADMIN", "USER"})
    public ResponseEntity<String> changePassword(HttpServletRequest request, @PathVariable String userId, @Valid @RequestBody final PasswordDto passwordDto) {
        User user = userDao.findById(userId);
        userDao.changePassword(user, passwordDto.getPassword());
        String message = messageSource.getMessage("message.resetPasswordSuc", null, request.getLocale());
        return new ResponseEntity<>(message, HttpStatus.OK);
    }

    @PutMapping(value = "/reSendConfirmation")
    @MyServiceAnnotation(name = "Kullanıcı Yeniden Doğrulama Kodu", path = "/reSendConfirmation", type = MethodType.PUT, permissionRoles = {"ADMIN", "USER"})
    public ResponseEntity<String> reSendConfirmation(HttpServletRequest request, @RequestBody final String userId) {
        User user = userDao.findById(userId);
        final Token token = tokenDao.prepareRegistrationAndPassword(user.getEmail(), TokenType.REGISTRATION, request);
        tokenDao.create(token);
        emailUtil.sendActivationEmail(request.getLocale(), user, token.getValue());
        String message = messageSource.getMessage("message.resendToken", null, request.getLocale());
        return new ResponseEntity<>(message, HttpStatus.OK);
    }

    @GetMapping(path = "/menus")
    @MyServiceAnnotation(name = "Kullanıcı Menülerini Listele", path = "/menus", type = MethodType.GET, permissionRoles = {"ADMIN", "USER"})
    public ResponseEntity<User> getUserAndMenus() {
        User user = userDao.findByEmail(securityUtil.getCurrentAuditor());
        List<Permission> permissions = permissionDao.findByTypeAndRolesIn(PermissionType.MENU, (List<Role>) user.getRoles());
        List<String> menuIds = permissions.stream()
                .map(Permission::getItemId)
                .collect(Collectors.toList());
        List<Menu> menus = menuDao.getMenusOfUser(menuIds);
        user.setMenus(menus);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }
}
