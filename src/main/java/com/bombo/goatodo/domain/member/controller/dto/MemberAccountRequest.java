package com.bombo.goatodo.domain.member.controller.dto;

import com.bombo.goatodo.domain.member.Account;
import com.bombo.goatodo.util.RegexPattern;
import jakarta.validation.constraints.Pattern;

public record MemberAccountRequest(

        @Pattern(regexp = RegexPattern.EMAIL_REGEX,
                message = "적절한 이메일 양식대로 입력해주세요. ex) goatodo@example.com")
        String email,

        @Pattern(regexp = RegexPattern.PASSWORD_REGEX,
                message = "영문자+숫자, 8자 이상 20자 이내이여야 합니다.")
        String password
) {

    public Account toAccount() {
        return Account.builder()
                .email(email)
                .password(password)
                .build();
    }
}
