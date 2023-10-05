package com.goatodo.application.user.dto.request;

import com.goatodo.domain.user.Occupation;
import com.goatodo.domain.user.Role;
import com.goatodo.domain.user.User;
import lombok.Builder;

@Builder
public record UserServiceCreateRequest(
        UserServiceAccountRequest account,
        String password,
        String nickName,
        Occupation occupation,
        Role role
) {

    public User toEntity() {
        return User.builder()
                .account(account.toVO())
                .nickname(nickName)
                .occupation(occupation)
                .role(role)
                .build();
    }
}
