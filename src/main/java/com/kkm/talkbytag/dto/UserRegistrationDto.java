package com.kkm.talkbytag.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

public class UserRegistrationDto {
    @NotBlank(message = "이메일은 필수사항입니다.")
    private String username;

    @NotBlank(message = "비밀번호는 필수사항입니다.")
    @Size(min = 8, max = 20, message = "비밀번호는 8자 이상 20자 이하로 작성해주세요.")
    @Pattern(regexp = "^(?=.*[!@#$%^&*(),.?\\\":{}|<>])[A-Za-z\\\\d!@#$%^&*(),.?\\\":{}|<>]*$", message = "비밀번호는 한개의 특수문자를 포함해주세요.")
    private String password;

    @NotBlank(message = "비밀번호 재입력은 필수사항입니다.")
    private String confirmPassword;

    public UserRegistrationDto(String username, String password, String confirmPassword) {
        this.username = username;
        this.password = password;
        this.confirmPassword = confirmPassword;
    }

    public UserRegistrationDto() {
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }
}
