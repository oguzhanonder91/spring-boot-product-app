package com.util.validation;

import com.common.dto.PasswordDto;
import com.common.dto.UserDto;
import com.util.SecurityUtil;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;


public class PasswordMatchesValidator implements ConstraintValidator<PasswordMatches, Object> {

    @Autowired
    private SecurityUtil securityUtil;

    @Override
    public void initialize(PasswordMatches constraintAnnotation) {

    }

    @Override
    public boolean isValid(Object obj, ConstraintValidatorContext constraintValidatorContext) {
        String password = null;
        String matchingPassword = null;
        if (obj instanceof UserDto) {
            UserDto user = (UserDto) obj;
            password = user.getPassword();
            matchingPassword = user.getMatchingPassword();
        } else if (obj instanceof PasswordDto) {
            PasswordDto passwordDto = (PasswordDto) obj;
            password = passwordDto.getPassword();
            matchingPassword = passwordDto.getMatchingPassword();
        }

        return securityUtil.decode(password).equals(securityUtil.decode(matchingPassword));
    }
}
