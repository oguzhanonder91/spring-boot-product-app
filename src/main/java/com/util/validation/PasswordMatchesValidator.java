package com.util.validation;

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
        UserDto user = (UserDto) securityUtil.decode(obj);
        return user.getPassword().equals(user.getMatchingPassword());
    }
}
