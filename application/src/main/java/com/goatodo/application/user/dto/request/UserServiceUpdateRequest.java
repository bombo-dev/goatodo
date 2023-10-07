package com.goatodo.application.user.dto.request;

import com.goatodo.domain.user.OccupationType;
import lombok.Builder;

@Builder
public record UserServiceUpdateRequest(
        String nickname,
        OccupationType occupationType
) {
}
