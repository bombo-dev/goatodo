package com.goatodo.application.user.dto;

import com.goatodo.common.util.FormatConverter;
import com.goatodo.domain.user.User;
import com.goatodo.domain.user.occupation.OccupationType;

public record UserResponse(
        Long id,
        String email,
        String password,
        String nickname,
        OccupationType occupationType,
        String createdAt,
        String updatedAt
) {
    public UserResponse(User user) {
        this(
                user.getId(),
                user.getAccount().getEmail(),
                user.getAccount().getPassword(),
                user.getNickname(),
                user.getOccupationType(),
                FormatConverter.convertLocalDateTimeToString(user.getCreatedAt()),
                FormatConverter.convertLocalDateTimeToString(user.getUpdatedAt())
        );
    }
}
