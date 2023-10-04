package com.goatodo.application.member.dto.request;

import com.goatodo.domain.member.Occupation;
import lombok.Builder;

@Builder
public record MemberServiceUpdateRequest(
        String nickname,
        Occupation occupation
) {
}
