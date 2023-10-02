package com.goatodo.domain.member;

import jakarta.validation.ConstraintViolation;
import org.assertj.core.api.Assertions;
import org.hibernate.validator.HibernateValidator;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import java.util.Set;

class MemberTest {


    private static LocalValidatorFactoryBean validator;

    @BeforeAll
    static void beforeAll() {
        validator = new LocalValidatorFactoryBean();
        validator.setProviderClass(HibernateValidator.class);
        validator.afterPropertiesSet();
    }

    @Test
    @DisplayName("회원의 계정 정보는 null 이면 검증 오류가 발생한다.")
    void memberAccountIsNotNullTest() {
        // given
        Member member = Member.builder()
                .nickname("봄보")
                .occupation(Occupation.GENERAL)
                .role(Role.NORMAL)
                .build();

        // when
        Set<ConstraintViolation<Member>> constraintViolation = validator.validate(member);

        // then
        Assertions.assertThat(constraintViolation).isNotEmpty();
        Assertions.assertThat(constraintViolation)
                .extracting(ConstraintViolation::getMessage)
                .containsOnly("회원의 계정은 null 일 수 없습니다.");
    }

    @Test
    @DisplayName("회원의 닉네임은 null 이면 검증 오류가 발생한다.")
    void memberNicknameIsNotNullTest() {
        // given
        Account account = Account.builder()
                .email("goatodo@email.com")
                .password("abcd1234")
                .build();

        Member member = Member.builder()
                .account(account)
                .occupation(Occupation.GENERAL)
                .role(Role.NORMAL)
                .build();

        // when
        Set<ConstraintViolation<Member>> constraintViolation = validator.validate(member);

        // then
        Assertions.assertThat(constraintViolation).isNotEmpty();
        Assertions.assertThat(constraintViolation)
                .extracting(ConstraintViolation::getMessage)
                .containsOnly("회원의 닉네임은 null 이거나 공백 일 수 없습니다.");
    }

    @Test
    @DisplayName("회원의 닉네임은 공백이면 검증 오류가 발생한다.")
    void memberNicknameIsNotBlankTest() {
        // given
        Account account = Account.builder()
                .email("goatodo@email.com")
                .password("abcd1234")
                .build();

        Member member = Member.builder()
                .account(account)
                .nickname("   ")
                .occupation(Occupation.GENERAL)
                .role(Role.NORMAL)
                .build();

        // when
        Set<ConstraintViolation<Member>> constraintViolation = validator.validate(member);

        // then
        Assertions.assertThat(constraintViolation).isNotEmpty();
        Assertions.assertThat(constraintViolation)
                .extracting(ConstraintViolation::getMessage)
                .containsOnly("회원의 닉네임은 null 이거나 공백 일 수 없습니다.");
    }

    @ParameterizedTest
    @ValueSource(strings = {"윽", "a", "15자가 초과된 닉네임을 작성"})
    @DisplayName("회원의 닉네임이 2자 미만 15자 초과면 검증 오류가 발생한다.")
    void memberNicknameOutOfRangeTest(String inputNickname) {
        // given
        Account account = Account.builder()
                .email("goatodo@email.com")
                .password("abcd1234")
                .build();

        Member member = Member.builder()
                .account(account)
                .nickname(inputNickname)
                .occupation(Occupation.GENERAL)
                .role(Role.NORMAL)
                .build();

        // when
        Set<ConstraintViolation<Member>> constraintViolation = validator.validate(member);

        // then
        Assertions.assertThat(constraintViolation).isNotEmpty();

        Assertions.assertThat(constraintViolation)
                .extracting(ConstraintViolation::getMessage)
                .containsOnly("닉네임은 2글자 이상 15자 이내여야 합니다.");
    }

    @ParameterizedTest
    @ValueSource(strings = {"닉네임", "내가 짱이야", "봄보", "15자 이내인 닉네임 작성."})
    @DisplayName("회원의 닉네임이 2자 이상 15자 이내이면 검증 오류가 존재하지 않는다.")
    void memberNicknameValidationSuccess(String inputNickname) {
        // given
        Account account = Account.builder()
                .email("goatodo@email.com")
                .password("abcd1234")
                .build();

        Member member = Member.builder()
                .account(account)
                .nickname(inputNickname)
                .occupation(Occupation.GENERAL)
                .role(Role.NORMAL)
                .build();

        // when
        Set<ConstraintViolation<Member>> validatedSet = validator.validate(member);

        // then
        Assertions.assertThat(validatedSet).isEmpty();
    }

    @Test
    @DisplayName("회원의 직업은 null 이면 안된다.")
    void memberOccupationIsNotNullTest() {
        // given
        Account account = Account.builder()
                .email("goatodo@email.com")
                .password("abcd1234")
                .build();

        Member member = Member.builder()
                .account(account)
                .nickname("봄보")
                .role(Role.NORMAL)
                .build();

        // when
        Set<ConstraintViolation<Member>> constraintViolation = validator.validate(member);

        // then
        Assertions.assertThat(constraintViolation).isNotEmpty();

        Assertions.assertThat(constraintViolation)
                .extracting(ConstraintViolation::getMessage)
                .containsOnly("회원의 직업은 null 일 수 없습니다.");
    }

    @Test
    @DisplayName("회원의 직업은 null 이 아니면 검증 오류가 발생하지 않는다.")
    void memberOccupationValidationSuccess() {
        // given
        Account account = Account.builder()
                .email("goatodo@email.com")
                .password("abcd1234")
                .build();

        Member member = Member.builder()
                .account(account)
                .nickname("봄보")
                .occupation(Occupation.GENERAL)
                .role(Role.NORMAL)
                .build();

        // when
        Set<ConstraintViolation<Member>> constraintViolation = validator.validate(member);

        // then
        Assertions.assertThat(constraintViolation).isEmpty();
    }

    @Test
    @DisplayName("회원의 권한이 null 이면 검증 오류가 발생한다.")
    void memberRoleIsNotNullTest() {

        // given
        Account account = Account.builder()
                .email("goatodo@email.com")
                .password("abcd1234")
                .build();

        Member member = Member.builder()
                .account(account)
                .nickname("봄보")
                .occupation(Occupation.GENERAL)
                .build();

        // when
        Set<ConstraintViolation<Member>> constraintViolation = validator.validate(member);

        // then
        Assertions.assertThat(constraintViolation).isNotEmpty();

        Assertions.assertThat(constraintViolation)
                .extracting(ConstraintViolation::getMessage)
                .containsOnly("회원의 역할은 null 일 수 없습니다.");
    }

    @Test
    @DisplayName("운영자 권한을 가진 계정이면 true 를 반환한다.")
    void memberRoleIsAdminTest() {

        // given
        Account account = Account.builder()
                .email("goatodo@email.com")
                .password("abcd1234")
                .build();

        Member member = Member.builder()
                .account(account)
                .nickname("봄보")
                .occupation(Occupation.GENERAL)
                .role(Role.ADMIN)
                .build();

        // when
        boolean isAdmin = member.isAdmin();

        // then
        Assertions.assertThat(isAdmin).isTrue();
    }

    @Test
    @DisplayName("일반 권한을 가진 회원이면 false 를 반환한다.")
    void memberRoleIsNotAdminTest() {

        // given
        Account account = Account.builder()
                .email("goatodo@email.com")
                .password("abcd1234")
                .build();

        Member member = Member.builder()
                .account(account)
                .nickname("봄보")
                .occupation(Occupation.GENERAL)
                .role(Role.NORMAL)
                .build();

        // when
        boolean isAdmin = member.isAdmin();

        // then
        Assertions.assertThat(isAdmin).isFalse();
    }

    @Test
    @DisplayName("회원을 정상적으로 생성하는 경우에 대한 테스트")
    void memberCreateTest() {

        // given
        Account account = Account.builder()
                .email("goatodo@email.com")
                .password("abcd1234")
                .build();

        Member member = Member.builder()
                .account(account)
                .nickname("봄보")
                .occupation(Occupation.GENERAL)
                .role(Role.NORMAL)
                .build();

        // when
        Set<ConstraintViolation<Member>> constraintViolation = validator.validate(member);

        // then
        Assertions.assertThat(constraintViolation).isEmpty();
    }
}