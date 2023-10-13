package com.goatodo.application.user.dto.request;

import com.goatodo.domain.user.Role;
import com.goatodo.domain.user.User;
import com.goatodo.domain.user.occupation.OccupationType;
import lombok.Builder;

@Builder
public record UserServiceCreateRequest(
        UserServiceAccountRequest account,
        String password,
        String nickName,
        OccupationType occupationType,
        Role role
) {

    public User toEntity() {
        return User.builder()
                .account(account.toVO())
                .nickname(nickName)
                .occupation(occupationType)
                .role(role)
                .build();
    }
}
