package com.example.tastefulai.global.config.auth;

import org.springframework.stereotype.Component;

import java.util.regex.Pattern;

@Component
public class SignUpValidation {

    /**
     * 비밀번호 유효성 검사
     * - 최소 8자 이상,
     * - 대문자 하나 이상 포함,
     * - 소문자 하나 이상 포함,
     * - 특수문자 하나 이상 포함
     */
    private final String PASSWORD_PATTERN =
            "^(?=.*[0-9])(?=.*[A-Z])(?=.*[a-z])(?=.*[!@#$%^&*()-+=]).{8,}$";

    // 이메일 유효성 검사 패턴
    private final String EMAIL_PATTERN =
            "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";

    // 비밀번호, 이메일 규칙 검사
    private final Pattern passwordPattern = Pattern.compile(PASSWORD_PATTERN);
    private final Pattern emailPattern = Pattern.compile(EMAIL_PATTERN);


    public boolean isValidPassword(String password) {
        return password != null && passwordPattern.matcher(password).matches();
    }

    public boolean isValidEmail(String email) {
        return email != null && emailPattern.matcher(email).matches();
    }
}
