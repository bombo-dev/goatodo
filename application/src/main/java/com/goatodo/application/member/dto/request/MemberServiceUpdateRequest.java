package com.goatodo.application.member.dto.request;

import com.goatodo.domain.member.Occupation;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public record MemberServiceUpdateRequest(
        String nickname,
        Occupation occupation
) {
}
