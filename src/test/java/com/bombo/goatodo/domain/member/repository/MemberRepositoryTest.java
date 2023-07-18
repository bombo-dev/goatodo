package com.bombo.goatodo.domain.member.repository;

import com.bombo.goatodo.domain.member.Account;
import com.bombo.goatodo.domain.member.Member;
import com.bombo.goatodo.domain.member.Occupation;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

@DataJpaTest
class MemberRepositoryTest {

    @Autowired
    private MemberRepository memberRepository;

    @Test
    @DisplayName("회원을 생성합니다.")
    void memberCreateSuccess() {
        // given
        Account account = Account.builder()
                .email("goatodo@email.com")
                .password("password1234")
                .build();

        Member member = Member.builder()
                .account(account)
                .nickname("고투두")
                .occupation(Occupation.GENERAL)
                .build();

        // when
        Member savedMember = memberRepository.save(member);
        Member findMember = memberRepository.findById(savedMember.getId()).get();

        // then
        Assertions.assertThat(savedMember).isEqualTo(findMember);
    }

    @Test
    @DisplayName("회원을 조회 할 수 있다.")
    void findMemberTest() {
        // given
        Account account = Account.builder()
                .email("goatodo@email.com")
                .password("password1234")
                .build();

        Member member = Member.builder()
                .account(account)
                .nickname("고투두")
                .occupation(Occupation.GENERAL)
                .build();

        Member savedMember = memberRepository.save(member);

        // when
        Member findMember = memberRepository.findById(savedMember.getId()).get();

        // then
        Assertions.assertThat(savedMember).isEqualTo(findMember);
    }

    @Test
    @DisplayName("전체 회원을 조회 할 수 있다.")
    void findMembers() {
        // given
        Account accountA = Account.builder()
                .email("goatodo@email.com")
                .password("password1234")
                .build();

        Account accountB = Account.builder()
                .email("goat@email.com")
                .password("password1234")
                .build();


        Member memberA = Member.builder()
                .account(accountA)
                .nickname("고투두")
                .occupation(Occupation.GENERAL)
                .build();

        Member memberB = Member.builder()
                .account(accountB)
                .nickname("고투두2")
                .occupation(Occupation.GENERAL)
                .build();

        memberRepository.save(memberA);
        memberRepository.save(memberB);

        // when
        List<Member> findMembers = memberRepository.findAll();

        // then
        Assertions.assertThat(findMembers).hasSize(2);
    }

    @Test
    @DisplayName("회원의 닉네임을 변경 할 수 있다.")
    void updateNickname() {
        // given
        Account account = Account.builder()
                .email("goatodo@email.com")
                .password("password1234")
                .build();

        Member member = Member.builder()
                .account(account)
                .nickname("고투두")
                .occupation(Occupation.GENERAL)
                .build();

        Member savedMember = memberRepository.save(member);

        // when
        memberRepository.updateNickname("투두변경", member.getId());
        Member findMember = memberRepository.findById(savedMember.getId()).get();

        // then
        Assertions.assertThat(findMember.getNickname()).isEqualTo("투두변경");
    }

    @Test
    @DisplayName("회원을 삭제 할 수 있다.")
    void deleteMember() {
        // given
        Account account = Account.builder()
                .email("goatodo@email.com")
                .password("password1234")
                .build();

        Member member = Member.builder()
                .account(account)
                .nickname("고투두")
                .occupation(Occupation.GENERAL)
                .build();

        Member savedMember = memberRepository.save(member);

        // when
        memberRepository.delete(savedMember);
        List<Member> findMembers = memberRepository.findAll();

        // then
        Assertions.assertThat(findMembers).isEmpty();
    }
}