package com.kkm.talkbytag.Validator;


import com.kkm.talkbytag.dto.UserRegistrationDto;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class UserRegistrationDtoValidator implements Validator {
    @Override
    public boolean supports(Class<?> clazz) {
        return UserRegistrationDto.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        UserRegistrationDto registrationDto = (UserRegistrationDto) target;

        // 기본 애노테이션 검사
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "username", "이메일은 필수사항입니다.");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "password", "비밀번호는 필수사항입니다.");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "confirmPassword", "비밀번호 재입력은 필수사항입니다.");

        // 비밀번호 길이 검사
        if (registrationDto.getPassword().length() < 8 || registrationDto.getPassword().length() > 20) {
            errors.rejectValue("password", "password.length", "비밀번호는 8자 이상 20자 이하로 작성해주세요.");
        }

        // 비밀번호 특수문자 검사
        Pattern pattern = Pattern.compile("^(?=.*[!@#$%^&*(),.?\":{}|<>])[A-Za-z\\d!@#$%^&*(),.?\":{}|<>]*$");
        Matcher matcher = pattern.matcher(registrationDto.getPassword());
        if (!matcher.matches()) {
            errors.rejectValue("password", "password.specialCharacter", "비밀번호는 한개의 특수문자를 포함해주세요.");
        }

        // 비밀번호와 확인 비밀번호 일치 검사
        if (!registrationDto.getPassword().equals(registrationDto.getConfirmPassword())) {
            errors.rejectValue("confirmPassword", "confirmPassword.match", "비밀번호와 비밀번호 확인이 일치하지 않습니다.");
        }
    }
}
