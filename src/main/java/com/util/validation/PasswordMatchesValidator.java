package com.util.validation;

import com.util.SecurityUtil;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;


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
        try {
            Method getMethodPassword = obj.getClass().getDeclaredMethod("getPassword");
            Method getMethodMatchingPassword = obj.getClass().getDeclaredMethod("getMatchingPassword");
            password = (String) getMethodPassword.invoke(obj);
            matchingPassword = (String) getMethodMatchingPassword.invoke(obj);
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            e.printStackTrace();
        }
        return securityUtil.decode(password).equals(securityUtil.decode(matchingPassword));
    }
}
