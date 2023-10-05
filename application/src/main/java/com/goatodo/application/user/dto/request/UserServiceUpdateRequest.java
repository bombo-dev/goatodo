package com.goatodo.application.user.dto.request;

import com.goatodo.domain.user.Occupation;
import lombok.Builder;

@Builder
public record UserServiceUpdateRequest(
        String nickname,
        Occupation occupation
) {
}
