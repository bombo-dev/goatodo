package com.goatodo.application.user.dto.request;

import com.goatodo.domain.user.Account;
import lombok.Builder;

@Builder
public record UserServiceAccountRequest(
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
