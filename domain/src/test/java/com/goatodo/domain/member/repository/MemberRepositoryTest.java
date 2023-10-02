package com.goatodo.domain.member.repository;

import com.bombo.goatodo.domain.member.Account;
import com.bombo.goatodo.domain.member.Member;
import com.bombo.goatodo.domain.member.Occupation;
import com.bombo.goatodo.domain.member.Role;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

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
                .role(Role.NORMAL)
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
                .role(Role.NORMAL)
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
                .role(Role.NORMAL)
                .build();

        Member memberB = Member.builder()
                .account(accountB)
                .nickname("고투두2")
                .occupation(Occupation.GENERAL)
                .role(Role.NORMAL)
                .build();

        memberRepository.save(memberA);
        memberRepository.save(memberB);

        // when
        List<Member> findMembers = memberRepository.findAll();

        // then
        Assertions.assertThat(findMembers).hasSize(2);
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
                .role(Role.NORMAL)
                .build();

        Member savedMember = memberRepository.save(member);

        // when
        memberRepository.delete(savedMember);
        List<Member> findMembers = memberRepository.findAll();

        // then
        Assertions.assertThat(findMembers).isEmpty();
    }

    @Test
    @DisplayName("닉네임으로 회원을 조회 할 수 있다.")
    void findByNicknameTest() {
        // given
        Account account = Account.builder()
                .email("goatodo@email.com")
                .password("password1234")
                .build();

        Member member = Member.builder()
                .account(account)
                .nickname("고투두")
                .occupation(Occupation.GENERAL)
                .role(Role.NORMAL)
                .build();

        Member savedMember = memberRepository.save(member);

        // when
        Optional<Member> findMember = memberRepository.findByNickname(savedMember.getNickname());

        // then
        Assertions.assertThat(findMember).isNotEmpty();
    }

    @Test
    @DisplayName("이메일로 회원을 조회 할 수 있다.")
    void findByEmailTest() {
        // given
        Account account = Account.builder()
                .email("goatodo@email.com")
                .password("password1234")
                .build();

        Member member = Member.builder()
                .account(account)
                .nickname("고투두")
                .occupation(Occupation.GENERAL)
                .role(Role.NORMAL)
                .build();

        Member savedMember = memberRepository.save(member);

        // when
        Optional<Member> findMember = memberRepository.findByAccount_Email(savedMember.getAccount().getEmail());

        // then
        Assertions.assertThat(findMember).isNotEmpty();
    }
}