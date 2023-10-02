package com.goatodo.domain.todo;

import com.bombo.goatodo.domain.member.Account;
import com.bombo.goatodo.domain.member.Member;
import com.bombo.goatodo.domain.member.Occupation;
import com.bombo.goatodo.domain.member.Role;
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

class TodoTest {

    private static LocalValidatorFactoryBean validator;
    private static Member member;
    private static Tag tag;

    @BeforeAll
    static void beforeAll() {
        validator = new LocalValidatorFactoryBean();
        validator.setProviderClass(HibernateValidator.class);
        validator.afterPropertiesSet();

        member = initMember();
        tag = initTag();
    }


    @Test
    @DisplayName("Todo를 작성한 Member가 없으면 검증 오류가 발생한다.")
    void createTodoNonMember() {
        // given
        Todo todo = Todo.builder()
                .tag(tag)
                .title("코딩 테스트")
                .description("프로그래머스 문제 풀이")
                .completeStatus(CompleteStatus.READY)
                .isActive(true)
                .build();

        // when
        Set<ConstraintViolation<Todo>> constraintViolations = validator.validate(todo);

        // then
        Assertions.assertThat(constraintViolations).isNotEmpty();
        Assertions.assertThat(constraintViolations)
                .extracting(ConstraintViolation::getMessage)
                .contains("Todo 작성 시 회원은 필수 입니다.");
    }

    @Test
    @DisplayName("Todo 작성 시 Tag가 없으면 검증 오류가 발생한다.")
    void createTodoNonTag() {
        // given
        Todo todo = Todo.builder()
                .member(member)
                .title("코딩 테스트")
                .description("프로그래머스 문제 풀이")
                .completeStatus(CompleteStatus.READY)
                .isActive(true)
                .build();

        // when
        Set<ConstraintViolation<Todo>> constraintViolations = validator.validate(todo);

        // then
        Assertions.assertThat(constraintViolations).isNotEmpty();
        Assertions.assertThat(constraintViolations)
                .extracting(ConstraintViolation::getMessage)
                .contains("Todo 작성 시 태그는 필수 입니다.");
    }

    @ParameterizedTest
    @ValueSource(strings = {"", " "})
    @DisplayName("Todo의 제목이 없으면 검증 오류가 발생한다.")
    void createTodoNonBlankTitle(String inputTitle) {
        // given
        Todo todo = Todo.builder()
                .member(member)
                .tag(tag)
                .title(inputTitle)
                .description("프로그래머스 문제 풀이")
                .completeStatus(CompleteStatus.READY)
                .isActive(true)
                .build();

        // when
        Set<ConstraintViolation<Todo>> constraintViolations = validator.validate(todo);

        // then
        Assertions.assertThat(constraintViolations).isNotEmpty();
        Assertions.assertThat(constraintViolations)
                .extracting(ConstraintViolation::getMessage)
                .containsOnly("Todo 작성 시 제목은 공백이거나 null 이면 안됩니다.");
    }

    @Test
    @DisplayName("제목의 길이는 20자를 초과 하면 검증 오류가 발생한다.")
    void TodoOutOfRangeTitle() {
        // given
        Tag tag = Tag.builder()
                .tagType(TagType.COMMON)
                .name("공부")
                .build();

        Todo todo = Todo.builder()
                .member(member)
                .tag(tag)
                .title("20자가 넘는 제목의 길이를 작성해봤습니다.")
                .description("프로그래머스 문제 풀이")
                .completeStatus(CompleteStatus.READY)
                .isActive(true)
                .build();

        // when
        Set<ConstraintViolation<Todo>> constraintViolations = validator.validate(todo);

        // then
        Assertions.assertThat(constraintViolations).isNotEmpty();
        Assertions.assertThat(constraintViolations)
                .extracting(ConstraintViolation::getMessage)
                .containsOnly("Todo의 제목의 길이는 20자를 초과 할 수 없습니다.");
    }

    @Test
    @DisplayName("Todo의 설명이 50자를 초과 할 수 없습니다.")
    void TodoOutOfRangeDescription() {
        // given
        Todo todo = Todo.builder()
                .member(member)
                .tag(tag)
                .title("코딩 테스트 연습")
                .description("프로그래머스의 문제를 풀 계획입니다. 프로그래머스 문제 중에서도 DP 관련된 문제를 풀 계획임.")
                .completeStatus(CompleteStatus.READY)
                .isActive(true)
                .build();

        // when
        Set<ConstraintViolation<Todo>> constraintViolations = validator.validate(todo);

        // then
        Assertions.assertThat(constraintViolations).isNotEmpty();
        Assertions.assertThat(constraintViolations)
                .extracting(ConstraintViolation::getMessage)
                .containsOnly("Todo의 설명은 50자를 초과 할 수 없습니다.");
    }

    @Test
    @DisplayName("Todo의 설명은 공백이 가능합니다.")
    void TodoDescriptionCanBlank() {
        // given
        Todo todo = Todo.builder()
                .member(member)
                .tag(tag)
                .title("코딩 테스트 연습")
                .description("")
                .completeStatus(CompleteStatus.READY)
                .isActive(true)
                .build();

        // when
        Set<ConstraintViolation<Todo>> constraintViolations = validator.validate(todo);

        // then
        Assertions.assertThat(constraintViolations).isEmpty();
    }

    @Test
    @DisplayName("Todo의 진행 상태가 null 이면 검증 오류가 발생한다.")
    void todoCompleteStatusIsNotNull() {
        // given
        Todo todo = Todo.builder()
                .member(member)
                .tag(tag)
                .title("코딩 테스트 연습")
                .description("프로그래머스 문제 풀이")
                .isActive(true)
                .build();

        // when
        Set<ConstraintViolation<Todo>> constraintViolations = validator.validate(todo);

        // then
        Assertions.assertThat(constraintViolations).isNotEmpty();
        Assertions.assertThat(constraintViolations)
                .extracting(ConstraintViolation::getMessage)
                .containsOnly("Todo 진행 상태는 null 일 수 없습니다.");
    }

    @Test
    @DisplayName("Todo를 정상적으로 생성한다.")
    void createTodoTest() {
        // given
        Todo todo = Todo.builder()
                .member(member)
                .tag(tag)
                .title("코딩 테스트 연습")
                .description("프로그래머스 문제 풀이")
                .completeStatus(CompleteStatus.READY)
                .isActive(true)
                .build();

        // when
        Set<ConstraintViolation<Todo>> constraintViolations = validator.validate(todo);

        // then
        Assertions.assertThat(constraintViolations).isEmpty();
    }

    static Member initMember() {
        Account account = Account.builder()
                .email("goatodo@naver.com")
                .password("password1234!")
                .build();

        return Member.builder()
                .account(account)
                .occupation(Occupation.GENERAL)
                .nickname("고투두")
                .role(Role.ADMIN)
                .build();
    }

    static Tag initTag() {
        return Tag.builder()
                .tagType(TagType.COMMON)
                .name("공부")
                .build();
    }
}