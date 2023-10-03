package com.goatodo.application.member.dto.request;

import com.goatodo.domain.member.Account;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public record MemberServiceAccountRequest(
        String email,
        String password
) {
    public Account toVo() {
        return Account.builder()
                .email(email)
                .password(password)
                .build();
    }
}
