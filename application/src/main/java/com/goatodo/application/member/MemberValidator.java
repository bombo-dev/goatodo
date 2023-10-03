package com.goatodo.application.member;

import com.goatodo.common.error.ErrorCode;
import com.goatodo.common.exception.DuplicateException;
import com.goatodo.domain.member.Member;
import com.goatodo.domain.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class MemberValidator {

    private final MemberRepository memberRepository;

    public void validateDuplicatedMember(Member member) {
        validateDuplicatedEmail(member.getAccount().getEmail());
        validateDuplicatedNickname(member.getNickname());
    }

    public void validateDuplicatedEmail(String email) {
        memberRepository.findByAccount_Email(email)
                .ifPresent(member -> {
                    throw new DuplicateException(ErrorCode.MEMBER_DUPLICATE_EMAIL);
                });
    }

    public void validateDuplicatedNickname(String nickname) {
        memberRepository.findByNickname(nickname)
                .ifPresent(member -> {
                    throw new DuplicateException(ErrorCode.MEMBER_DUPLICATE_NICKNAME);
                });
    }
}
