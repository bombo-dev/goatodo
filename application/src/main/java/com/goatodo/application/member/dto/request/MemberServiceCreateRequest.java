package com.goatodo.application.member.dto.request;

import com.goatodo.domain.member.Member;
import com.goatodo.domain.member.Occupation;
import com.goatodo.domain.member.Role;
import lombok.Builder;

@Builder
public record MemberServiceCreateRequest(
        MemberServiceAccountRequest account,
        String password,
        String nickName,
        Occupation occupation,
        Role role
) {

    public Member toEntity() {
        return Member.builder()
                .account(account.toVO())
                .nickname(nickName)
                .occupation(occupation)
                .role(role)
                .build();
    }
}
