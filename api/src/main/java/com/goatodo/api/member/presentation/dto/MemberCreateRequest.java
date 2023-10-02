package com.goatodo.api.member.presentation.dto;

import com.bombo.goatodo.domain.member.Account;
import com.bombo.goatodo.domain.member.Member;
import com.bombo.goatodo.domain.member.Occupation;
import com.bombo.goatodo.domain.member.Role;
import com.bombo.goatodo.util.RegexPattern;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import org.hibernate.validator.constraints.Length;

public record MemberCreateRequest(
        @Length(min = 5, max = 30, message = "이메일 아이디는 30자 이내여야 합니다.")
        String emailId,

        @Pattern(regexp = RegexPattern.DOMAIN_NAME_REGEX, message = "이메일 양식에 맞게 입력해주세요. ex)abcd.com")
        String domainName,

        @Pattern(regexp = RegexPattern.PASSWORD_REGEX, message = "영문자+숫자, 8자 이상 20자 이내이여야 합니다.")
        String password,

        @NotBlank(message = "회원의 닉네임은 null 이거나 공백 일 수 없습니다.")
        @Length(min = 2, max = 15, message = "닉네임은 2글자 이상 15자 이내여야 합니다.")
        String nickname,

        @NotNull(message = "회원의 직업은 null 일 수 없습니다.")
        Occupation occupation,

        Role role
) {
    public Member toEntity() {
        return Member.builder()
                .account(createAccount(emailId, domainName, password))
                .nickname(nickname)
                .occupation(occupation)
                .role(role)
                .build();
    }

    private Account createAccount(String emailId, String domainName, String password) {
        String email = concatenateEmail(emailId, domainName);
        return new Account(email, password);
    }

    private String concatenateEmail(String emailId, String domainName) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(emailId);
        stringBuilder.append("@");
        stringBuilder.append(domainName);
        return stringBuilder.toString();
    }
}
