package com.goatodo.application.user.dto.request;

import com.goatodo.domain.user.occupation.OccupationType;
import lombok.Builder;

@Builder
public record UserServiceUpdateRequest(
        String nickname,
        OccupationType occupationType
) {
}
