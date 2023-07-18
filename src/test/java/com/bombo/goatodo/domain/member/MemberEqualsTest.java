package com.bombo.goatodo.domain.member;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class MemberEqualsTest {

    @Test
    @DisplayName("회원 계정 정보의 아이디와 비밀번호가 둘 다 일치하지 않으면 같은 회원이 아니다.")
    void notSameMemberWithDifferentEmailAndPasswordTest() {
        // given
        Account accountA = Account.builder()
                .email("bombo@email.com")
                .password("password1234")
                .build();

        Account accountB = Account.builder()
                .email("abcde@email.com")
                .password("password22")
                .build();

        Member memberA = Member.builder()
                .account(accountA)
                .nickname("회원A")
                .occupation(Occupation.GENERAL)
                .build();

        Member memberB = Member.builder()
                .account(accountB)
                .nickname("회원B")
                .occupation(Occupation.GENERAL)
                .build();

        // when
        boolean isSameMember = memberA.isSameMember(memberB.getAccount());

        // then
        Assertions.assertThat(isSameMember).isFalse();
    }

    @Test
    @DisplayName("회원 계정 정보의 비밀번호가 일치해도 같은 회원이 아니다.")
    void notSameMemberWithSamePasswordTest() {
        // given
        Account accountA = Account.builder()
                .email("bombo@email.com")
                .password("password1234")
                .build();

        Account accountB = Account.builder()
                .email("abcde@email.com")
                .password("password1234")
                .build();

        Member memberA = Member.builder()
                .account(accountA)
                .nickname("회원A")
                .occupation(Occupation.GENERAL)
                .build();

        Member memberB = Member.builder()
                .account(accountB)
                .nickname("회원B")
                .occupation(Occupation.GENERAL)
                .build();

        // when
        boolean isSameMember = memberA.isSameMember(memberB.getAccount());

        // then
        Assertions.assertThat(isSameMember).isFalse();
    }

    @Test
    @DisplayName("회원 계정 정보의 아이디와 비밀번호가 일치하면 같은 회원이다.")
    void SameMemberTest() {
        // given
        Account accountA = Account.builder()
                .email("bombo@email.com")
                .password("password1234")
                .build();

        Account accountB = Account.builder()
                .email("bombo@email.com")
                .password("password1234")
                .build();

        Member memberA = Member.builder()
                .account(accountA)
                .nickname("회원A")
                .occupation(Occupation.GENERAL)
                .build();

        Member memberB = Member.builder()
                .account(accountB)
                .nickname("회원A")
                .occupation(Occupation.GENERAL)
                .build();

        // when
        boolean isSameMember = memberA.isSameMember(memberB.getAccount());

        // then
        Assertions.assertThat(isSameMember).isTrue();
    }
}
