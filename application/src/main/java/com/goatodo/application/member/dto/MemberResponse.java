package com.goatodo.application.member.dto;

import com.bombo.goatodo.domain.member.Member;
import com.bombo.goatodo.domain.member.Occupation;
import com.bombo.goatodo.util.FormatConverter;

public record MemberResponse(
        Long id,
        String email,
        String password,
        String nickname,
        Occupation occupation,
        String createdAt,
        String updatedAt
) {
    public MemberResponse(Member member) {
        this(
                member.getId(),
                member.getAccount().getEmail(),
                member.getAccount().getPassword(),
                member.getNickname(),
                member.getOccupation(),
                FormatConverter.convertLocalDateTimeToString(member.getCreatedAt()),
                FormatConverter.convertLocalDateTimeToString(member.getUpdatedAt())
        );
    }
}
