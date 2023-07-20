package com.bombo.goatodo.domain.category;

import com.bombo.goatodo.domain.member.Account;
import com.bombo.goatodo.domain.member.Member;
import com.bombo.goatodo.domain.member.Occupation;
import com.bombo.goatodo.domain.todo.Category;
import jakarta.validation.ConstraintViolation;
import org.assertj.core.api.Assertions;
import org.hibernate.validator.HibernateValidator;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import java.util.Set;

class CategoryTest {

    private static LocalValidatorFactoryBean validator;
    private Member member;

    @BeforeAll
    static void beforeAll() {
        validator = new LocalValidatorFactoryBean();
        validator.setProviderClass(HibernateValidator.class);
        validator.afterPropertiesSet();
    }

    @BeforeEach
    void setUp() {
        Account account = Account.builder()
                .email("email@email.com")
                .password("password1234")
                .build();

        member = Member.builder()
                .account(account)
                .nickname("닉네임")
                .occupation(Occupation.GENERAL)
                .build();
    }

    @Test
    @DisplayName("카테고리의 멤버는 null 일 수 있다.")
    void categoryMemberIsNullTest() {
        // given
        Category category = Category.builder()
                .id(0L)
                .tag("카테고리 태그")
                .build();

        // when
        Set<ConstraintViolation<Category>> constraintViolations = validator.validate(category);

        // then
        Assertions.assertThat(constraintViolations).isEmpty();
    }

    @ParameterizedTest
    @ValueSource(strings = {"놉", "20자가 초과하는 태그를 한 번 만들어보았습니다."})
    @DisplayName("카테고리 태그가 2자 미만 20자를 초과하면 안된다.")
    void categoryTagOutOfRangeTest(String tag) {
        // given
        Category category = Category.builder()
                .id(0L)
                .tag(tag)
                .member(member)
                .build();

        // when
        Set<ConstraintViolation<Category>> constraintViolations = validator.validate(category);

        // then
        Assertions.assertThat(constraintViolations).isNotEmpty();
    }

    @Test
    @DisplayName("카테고리 태그는 공백 일 수 없습니다.")
    void categoryTagIsNotBlank() {
        // given
        Category category = Category.builder()
                .id(0L)
                .tag("   ")
                .member(member)
                .build();

        // when
        Set<ConstraintViolation<Category>> constraintViolations = validator.validate(category);

        // then
        Assertions.assertThat(constraintViolations).isNotEmpty();
    }

    @ParameterizedTest
    @ValueSource(strings = {"공부", "스터디", "일상", "약속"})
    @DisplayName("카테고리 태그가 2자 이상 20자 이내면 검증 오류가 발생하지 않는다.")
    void categoryTagTest(String tag) {
        // given
        Category category = Category.builder()
                .id(0L)
                .tag(tag)
                .member(member)
                .build();

        // when
        Set<ConstraintViolation<Category>> constraintViolations = validator.validate(category);

        // then
        Assertions.assertThat(constraintViolations).isEmpty();
    }
}