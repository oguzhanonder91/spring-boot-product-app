package com.util.validation;

import com.common.dto.UserDto;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

public class UserValidator implements Validator {

    @Override
    public boolean supports( Class<?> clazz) {
        return UserDto.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate( Object obj,  Errors errors) {
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "name", "message.name", "Name is required.");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "surname", "message.lastName", "Surname is required.");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "password", "message.password", "Password is required.");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "email", "message.username", "UserName is required.");
    }
}
