package com.bombo.goatodo.domain.member.service;

import com.bombo.goatodo.domain.member.Occupation;
import com.bombo.goatodo.domain.member.Role;
import com.bombo.goatodo.domain.member.controller.dto.MemberAccountRequest;
import com.bombo.goatodo.domain.member.controller.dto.MemberCreateRequest;
import com.bombo.goatodo.domain.member.controller.dto.MemberUpdateRequest;
import com.bombo.goatodo.domain.member.service.dto.MemberResponse;
import com.bombo.goatodo.domain.member.service.dto.MembersResponse;
import com.bombo.goatodo.global.exception.DuplicateException;
import com.bombo.goatodo.global.exception.NotExistIdRequestException;
import com.bombo.goatodo.global.exception.RoleException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
class MemberServiceTest {

    @Autowired
    private MemberService memberService;

    @Test
    @DisplayName("회원의 닉네임이 중복되면 예외가 발생한다.")
    void saveMemberDuplicateNicknameEx() {
        // given
        MemberCreateRequest memberCreateRequestA = new MemberCreateRequest(
                "email",
                "naver.com",
                "password1234",
                "고투두",
                Occupation.GENERAL,
                Role.NORMAL
        );

        MemberCreateRequest memberCreateRequestB = new MemberCreateRequest(
                "email2",
                "naver.com",
                "password1234!",
                "고투두",
                Occupation.GENERAL,
                Role.NORMAL
        );
        memberService.save(memberCreateRequestA);

        // when

        // then
        Assertions.assertThatThrownBy(() -> memberService.save(memberCreateRequestB))
                .isInstanceOf(DuplicateException.class)
                .hasMessage("닉네임이 중복되었습니다.");
    }

    @Test
    @DisplayName("회원의 이메일이 중복되면 예외가 발생한다.")
    void saveMemberDuplicateEmailEx() {
        // given
        MemberCreateRequest memberCreateRequestA = new MemberCreateRequest(
                "email",
                "naver.com",
                "password1234",
                "고투두",
                Occupation.GENERAL,
                Role.NORMAL
        );

        MemberCreateRequest memberCreateRequestB = new MemberCreateRequest(
                "email",
                "naver.com",
                "password1234!",
                "고투두2",
                Occupation.GENERAL,
                Role.NORMAL
        );
        memberService.save(memberCreateRequestA);

        // when

        // then
        Assertions.assertThatThrownBy(() -> memberService.save(memberCreateRequestB))
                .isInstanceOf(DuplicateException.class)
                .hasMessage("이메일이 중복되었습니다.");
    }

    @Test
    @DisplayName("DTO를 받아 회원을 생성하고 반환한다.")
    void saveMemberTest() {
        // given
        MemberCreateRequest memberCreateRequest = new MemberCreateRequest(
                "email",
                "naver.com",
                "password1234",
                "고투두",
                Occupation.GENERAL,
                Role.NORMAL
        );

        // when
        Long savedId = memberService.save(memberCreateRequest);
        MemberResponse findMember = memberService.findOne(savedId);

        // then
        Assertions.assertThat(savedId).isEqualTo(findMember.id());
    }

    @Test
    @DisplayName("회원 조회 시 id가 전달되지 않으면 예외가 발생한다.")
    void findMemberIsNullIdEx() {
        // given
        MemberCreateRequest memberCreateRequest = new MemberCreateRequest(
                "email",
                "naver.com",
                "password1234",
                "고투두",
                Occupation.GENERAL,
                Role.NORMAL
        );

        // when
        memberService.save(memberCreateRequest);

        // then
        Assertions.assertThatThrownBy(() -> memberService.findOne(null))
                .isInstanceOf(InvalidDataAccessApiUsageException.class)
                .hasMessage("The given id must not be null");
    }

    @Test
    @DisplayName("회원 조회 시 존재하지 않는 id가 전달되면 예외가 발생한다.")
    void findMemberIsNotExistIdEx() {
        // given
        MemberCreateRequest memberCreateRequest = new MemberCreateRequest(
                "email",
                "naver.com",
                "password1234",
                "고투두",
                Occupation.GENERAL,
                Role.NORMAL
        );

        // when
        memberService.save(memberCreateRequest);

        // then
        Assertions.assertThatThrownBy(() -> memberService.findOne(-1L))
                .isInstanceOf(NotExistIdRequestException.class)
                .hasMessage("잘못된 ID가 전달되었습니다.");
    }


    @Test
    @DisplayName("회원을 정상적으로 조회 할 수 있다.")
    void findMemberTest() {
        // given
        MemberCreateRequest memberCreateRequest = new MemberCreateRequest(
                "email",
                "naver.com",
                "password1234",
                "고투두",
                Occupation.GENERAL,
                Role.NORMAL
        );

        // when
        Long savedId = memberService.save(memberCreateRequest);
        MemberResponse findMember = memberService.findOne(savedId);

        // then
        Assertions.assertThat(savedId).isEqualTo(findMember.id());
    }

    @Test
    @DisplayName("회원을 전체 조회 시 아무도 없다면 empty가 반환된다.")
    void findAllMemberEmptyTest() {
        // given

        // when
        MembersResponse findMembers = memberService.findAll();

        // then
        Assertions.assertThat(findMembers.getMemberResponseList()).isEmpty();
    }

    @Test
    @DisplayName("회원을 전체 조회 할 수 있다.")
    void findAllMemberTest() {
        // given
        MemberCreateRequest memberCreateRequestA = new MemberCreateRequest(
                "email",
                "naver.com",
                "password1234",
                "고투두",
                Occupation.GENERAL,
                Role.NORMAL
        );

        MemberCreateRequest memberCreateRequestB = new MemberCreateRequest(
                "goatodo",
                "naver.com",
                "password1234",
                "고투두2",
                Occupation.GENERAL,
                Role.NORMAL
        );

        memberService.save(memberCreateRequestA);
        memberService.save(memberCreateRequestB);

        // when
        MembersResponse findMembers = memberService.findAll();

        // then
        Assertions.assertThat(findMembers.getMemberResponseList()).isNotEmpty();
        Assertions.assertThat(findMembers.getMemberResponseList()).hasSize(2);
    }

    @Test
    @DisplayName("회원 프로필 변경 시 중복된 nickname이면 예외가 발생한다.")
    void duplicateMemberNicknameEx() {
        // given
        MemberCreateRequest memberCreateRequestA = new MemberCreateRequest(
                "email",
                "naver.com",
                "password1234",
                "고투두",
                Occupation.GENERAL,
                Role.NORMAL
        );

        MemberCreateRequest memberCreateRequestB = new MemberCreateRequest(
                "goatodo",
                "naver.com",
                "password1234",
                "고투두2",
                Occupation.GENERAL,
                Role.NORMAL
        );
        memberService.save(memberCreateRequestA);
        Long savedId = memberService.save(memberCreateRequestB);

        MemberUpdateRequest memberUpdateRequest = new MemberUpdateRequest(
                "고투두",
                Occupation.ELEMENTARY_SCHOOL_STUDENT
        );

        // when

        // then
        Assertions.assertThatThrownBy(() -> memberService.updateProfile(savedId, memberUpdateRequest))
                .isInstanceOf(DuplicateException.class)
                .hasMessage("닉네임이 중복되었습니다.");
    }

    @Test
    @DisplayName("회원 프로필을 정상적으로 변경 할 수 있다.")
    void memberProfileUpdateTest() {
        // given
        MemberCreateRequest memberCreateRequest = new MemberCreateRequest(
                "email",
                "naver.com",
                "password1234",
                "고투두",
                Occupation.GENERAL,
                Role.NORMAL
        );
        Long savedId = memberService.save(memberCreateRequest);

        // when
        MemberUpdateRequest memberUpdateRequest = new MemberUpdateRequest(
                "희귀닉네임",
                Occupation.ELEMENTARY_SCHOOL_STUDENT
        );

        memberService.updateProfile(savedId, memberUpdateRequest);
        MemberResponse findMember = memberService.findOne(savedId);

        // then
        Assertions.assertThat(findMember.id()).isEqualTo(savedId);
        Assertions.assertThat(findMember.nickname()).isEqualTo("희귀닉네임");
        Assertions.assertThat(findMember.occupation()).isEqualTo(Occupation.ELEMENTARY_SCHOOL_STUDENT);
    }

    @Test
    @DisplayName("회원의 패스워드 변경 시 이메일이 일치하지 않으면 예외가 발생한다.")
    void passwordUpdateWithNotSameEmailEx() {
        // given
        MemberCreateRequest memberCreateRequest = new MemberCreateRequest(
                "email",
                "naver.com",
                "password1234",
                "고투두",
                Occupation.GENERAL,
                Role.NORMAL
        );
        Long savedId = memberService.save(memberCreateRequest);

        MemberAccountRequest memberAccountRequest =
                new MemberAccountRequest("new@email.com", "password123");

        // when

        // then
        Assertions.assertThatThrownBy(() -> memberService.updatePassword(savedId, memberAccountRequest))
                .isInstanceOf(RoleException.class)
                .hasMessage("수정 할 수 있는 권한이 없습니다.");
    }

    @Test
    @DisplayName("동일한 패스워드로 변경을 시도하면 예외가 발생한다.")
    void duplicatePasswordUpdateTest() {
        // given
        MemberCreateRequest memberCreateRequest = new MemberCreateRequest(
                "email",
                "naver.com",
                "password1234",
                "고투두",
                Occupation.GENERAL,
                Role.NORMAL
        );
        Long savedId = memberService.save(memberCreateRequest);
        MemberResponse findMember = memberService.findOne(savedId);

        // when

        MemberAccountRequest memberAccountRequest =
                new MemberAccountRequest(findMember.email(), "password1234");

        // then
        Assertions.assertThatThrownBy(() -> memberService.updatePassword(findMember.id(), memberAccountRequest))
                .isInstanceOf(DuplicateException.class)
                .hasMessage("동일한 패스워드로 변경은 불가능합니다.");
    }

    @Test
    @DisplayName("회원은 정상적으로 패스워드를 변경 할 수 있다.")
    void passwordUpdateTest() {
        // given
        MemberCreateRequest memberCreateRequest = new MemberCreateRequest(
                "email",
                "naver.com",
                "password1234",
                "고투두",
                Occupation.GENERAL,
                Role.NORMAL
        );
        Long savedId = memberService.save(memberCreateRequest);
        MemberResponse findMember = memberService.findOne(savedId);

        // when
        MemberAccountRequest memberAccountRequest =
                new MemberAccountRequest(findMember.email(), "password123");
        memberService.updatePassword(findMember.id(), memberAccountRequest);

        MemberResponse updatedMember = memberService.findOne(savedId);

        // then
        Assertions.assertThat(updatedMember.password()).isEqualTo("password123");
    }
}