package com.goatodo.application.user.dto;

import com.goatodo.common.util.FormatConverter;
import com.goatodo.domain.user.Occupation;
import com.goatodo.domain.user.User;

public record UserResponse(
        Long id,
        String email,
        String password,
        String nickname,
        Occupation occupation,
        String createdAt,
        String updatedAt
) {
    public UserResponse(User user) {
        this(
                user.getId(),
                user.getAccount().getEmail(),
                user.getAccount().getPassword(),
                user.getNickname(),
                user.getOccupation(),
                FormatConverter.convertLocalDateTimeToString(user.getCreatedAt()),
                FormatConverter.convertLocalDateTimeToString(user.getUpdatedAt())
        );
    }
}
