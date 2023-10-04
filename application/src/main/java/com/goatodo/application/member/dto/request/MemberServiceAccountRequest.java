package com.goatodo.application.member.dto.request;

import com.goatodo.domain.member.Account;
import lombok.Builder;

@Builder
public record MemberServiceAccountRequest(
        String email,
        String password
) {
    public Account toVO() {
        return Account.builder()
                .email(email)
                .password(password)
                .build();
    }
}
