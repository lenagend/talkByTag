package com.kkm.talkbytag.Validator;

import com.kkm.talkbytag.dto.UserChangePasswordDto;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;

import org.springframework.validation.Validator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class UserChangePasswordDtoValidator implements Validator {

    @Override
    public boolean supports(Class<?> clazz) {
        return UserChangePasswordDto.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        UserChangePasswordDto changePasswordDto = (UserChangePasswordDto) target;

        // 기본 애노테이션 검사
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "currentPassword", "현재 비밀번호는 필수사항입니다.");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "newPassword", "새 비밀번호는 필수사항입니다.");

        // 비밀번호 길이 검사
        if (changePasswordDto.getNewPassword().length() < 8 || changePasswordDto.getNewPassword().length() > 20) {
            errors.rejectValue("newPassword", "newPassword.length", "새 비밀번호는 8자 이상 20자 이하로 작성해주세요.");
        }

        // 비밀번호 특수문자 검사
        Pattern pattern = Pattern.compile("^(?=.*[!@#$%^&*(),.?\":{}|<>])[A-Za-z\\d!@#$%^&*(),.?\":{}|<>]*$");
        Matcher matcher = pattern.matcher(changePasswordDto.getNewPassword());
        if (!matcher.matches()) {
            errors.rejectValue("newPassword", "newPassword.specialCharacter", "새 비밀번호는 한 개의 특수문자를 포함해주세요.");
        }
    }
}
