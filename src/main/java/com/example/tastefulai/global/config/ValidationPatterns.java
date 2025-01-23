package com.example.tastefulai.global.config;

import java.util.regex.Pattern;

public class ValidationPatterns {

    // 비밀번호 및 이메일 정규식 상수화
    public static final Pattern PASSWORD_PATTERN = Pattern.compile("^(?=.*[A-Z])(?=.*[0-9])(?=.*[a-z])(?=.*[!@#$%^&*()-+=]).{8,}$");
    public static final Pattern EMAIL_PATTERN = Pattern.compile( "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$");
}