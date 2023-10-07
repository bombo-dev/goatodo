package com.goatodo.application.user;

import com.goatodo.domain.user.OccupationType;
import com.goatodo.domain.user.Role;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
class UserServiceTest {

    @Autowired
    private UserService userService;

    @Test
    @DisplayName("회원의 닉네임이 중복되면 예외가 발생한다.")
    void saveMemberDuplicateNicknameEx() {
        // given
        MemberCreateRequest memberCreateRequestA = new MemberCreateRequest(
                "email",
                "naver.com",
                "password1234",
                "고투두",
                OccupationType.GENERAL,
                Role.NORMAL
        );

        MemberCreateRequest memberCreateRequestB = new MemberCreateRequest(
                "email2",
                "naver.com",
                "password1234!",
                "고투두",
                OccupationType.GENERAL,
                Role.NORMAL
        );
        userService.save(memberCreateRequestA);

        // when

        // then
        Assertions.assertThatThrownBy(() -> userService.save(memberCreateRequestB))
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
                OccupationType.GENERAL,
                Role.NORMAL
        );

        MemberCreateRequest memberCreateRequestB = new MemberCreateRequest(
                "email",
                "naver.com",
                "password1234!",
                "고투두2",
                OccupationType.GENERAL,
                Role.NORMAL
        );
        userService.save(memberCreateRequestA);

        // when

        // then
        Assertions.assertThatThrownBy(() -> userService.save(memberCreateRequestB))
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
                OccupationType.GENERAL,
                Role.NORMAL
        );

        // when
        Long savedId = userService.save(memberCreateRequest);
        MemberResponse findMember = userService.findOne(savedId);

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
                OccupationType.GENERAL,
                Role.NORMAL
        );

        // when
        userService.save(memberCreateRequest);

        // then
        Assertions.assertThatThrownBy(() -> userService.findOne(null))
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
                OccupationType.GENERAL,
                Role.NORMAL
        );

        // when
        userService.save(memberCreateRequest);

        // then
        Assertions.assertThatThrownBy(() -> userService.findOne(-1L))
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
                OccupationType.GENERAL,
                Role.NORMAL
        );

        // when
        Long savedId = userService.save(memberCreateRequest);
        MemberResponse findMember = userService.findOne(savedId);

        // then
        Assertions.assertThat(savedId).isEqualTo(findMember.id());
    }

    @Test
    @DisplayName("회원을 전체 조회 시 아무도 없다면 empty가 반환된다.")
    void findAllMemberEmptyTest() {
        // given

        // when
        MembersResponse findMembers = userService.findAll();

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
                OccupationType.GENERAL,
                Role.NORMAL
        );

        MemberCreateRequest memberCreateRequestB = new MemberCreateRequest(
                "goatodo",
                "naver.com",
                "password1234",
                "고투두2",
                OccupationType.GENERAL,
                Role.NORMAL
        );

        userService.save(memberCreateRequestA);
        userService.save(memberCreateRequestB);

        // when
        MembersResponse findMembers = userService.findAll();

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
                OccupationType.GENERAL,
                Role.NORMAL
        );

        MemberCreateRequest memberCreateRequestB = new MemberCreateRequest(
                "goatodo",
                "naver.com",
                "password1234",
                "고투두2",
                OccupationType.GENERAL,
                Role.NORMAL
        );
        userService.save(memberCreateRequestA);
        Long savedId = userService.save(memberCreateRequestB);

        MemberUpdateRequest memberUpdateRequest = new MemberUpdateRequest(
                "고투두",
                OccupationType.ELEMENTARY_SCHOOL_STUDENT
        );

        // when

        // then
        Assertions.assertThatThrownBy(() -> userService.updateProfile(savedId, memberUpdateRequest))
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
                OccupationType.GENERAL,
                Role.NORMAL
        );
        Long savedId = userService.save(memberCreateRequest);

        // when
        MemberUpdateRequest memberUpdateRequest = new MemberUpdateRequest(
                "희귀닉네임",
                OccupationType.ELEMENTARY_SCHOOL_STUDENT
        );

        userService.updateProfile(savedId, memberUpdateRequest);
        MemberResponse findMember = userService.findOne(savedId);

        // then
        Assertions.assertThat(findMember.id()).isEqualTo(savedId);
        Assertions.assertThat(findMember.nickname()).isEqualTo("희귀닉네임");
        Assertions.assertThat(findMember.occupation()).isEqualTo(OccupationType.ELEMENTARY_SCHOOL_STUDENT);
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
                OccupationType.GENERAL,
                Role.NORMAL
        );
        Long savedId = userService.save(memberCreateRequest);

        MemberAccountRequest memberAccountRequest =
                new MemberAccountRequest("new@email.com", "password123");

        // when

        // then
        Assertions.assertThatThrownBy(() -> userService.updatePassword(savedId, memberAccountRequest))
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
                OccupationType.GENERAL,
                Role.NORMAL
        );
        Long savedId = userService.save(memberCreateRequest);
        MemberResponse findMember = userService.findOne(savedId);

        // when

        MemberAccountRequest memberAccountRequest =
                new MemberAccountRequest(findMember.email(), "password1234");

        // then
        Assertions.assertThatThrownBy(() -> userService.updatePassword(findMember.id(), memberAccountRequest))
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
                OccupationType.GENERAL,
                Role.NORMAL
        );
        Long savedId = userService.save(memberCreateRequest);
        MemberResponse findMember = userService.findOne(savedId);

        // when
        MemberAccountRequest memberAccountRequest =
                new MemberAccountRequest(findMember.email(), "password123");
        userService.updatePassword(findMember.id(), memberAccountRequest);

        MemberResponse updatedMember = userService.findOne(savedId);

        // then
        Assertions.assertThat(updatedMember.password()).isEqualTo("password123");
    }
}