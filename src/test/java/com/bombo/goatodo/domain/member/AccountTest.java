package com.bombo.goatodo.domain.member;

import jakarta.validation.ConstraintViolation;
import org.assertj.core.api.Assertions;
import org.hibernate.validator.HibernateValidator;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import java.util.Iterator;
import java.util.Set;

class AccountTest {

    private static LocalValidatorFactoryBean validator;

    @BeforeAll
    static void beforeAll() {
        validator = new LocalValidatorFactoryBean();
        validator.setProviderClass(HibernateValidator.class);
        validator.afterPropertiesSet();
    }

    @ParameterizedTest
    @ValueSource(strings = {"email", "email@email", "email.com", "", " "})
    @DisplayName("계정 Email은 Email 표기법에 맞게 작성되지 않는다면 위반된 검증사항이 존재한다.")
    void accountEmailValidationFail(String inputEmail) {
        // given
        Account account = Account.builder()
                .email(inputEmail)
                .password("abcd1234")
                .build();

        // when
        Set<ConstraintViolation<Account>> validatedSet = validator.validate(account);

        Iterator<ConstraintViolation<Account>> validatedIterator = validatedSet.iterator();
        ConstraintViolation<Account> constraintViolation = validatedIterator.next();

        // then
        Assertions.assertThat(validatedSet).isNotEmpty();

        Assertions.assertThat(constraintViolation.getMessage())
                .isEqualTo("적절한 이메일 양식대로 입력해주세요. ex) goatodo@example.com");
    }

    @ParameterizedTest
    @ValueSource(strings = {"email@email.com", "email@email.net", "email@email.co.kr", "email@email.edu"})
    @DisplayName("계정 Email은 Email 표기법에 맞게 작성되면 위반된 검증사항이 존재하지 않는다.")
    void accountEmailValidationSuccess(String inputEmail) {
        // given
        Account account = Account.builder()
                .email(inputEmail)
                .password("abcd1234")
                .build();

        // when
        Set<ConstraintViolation<Account>> validatedSet = validator.validate(account);

        // then
        Assertions.assertThat(validatedSet).isEmpty();
    }

    @ParameterizedTest
    @ValueSource(strings = {"abcd123", "abcdefghijk1234567890", "", " "})
    @DisplayName("계정 password의 길이가 적절한 8자 미만 20자 초과라면 위반된 검증사항이 존재한다.")
    void accountPasswordLengthValidationFail(String inputPassword) {
        // given
        Account account = Account.builder()
                .email("goatodo@example.com")
                .password(inputPassword)
                .build();

        // when
        Set<ConstraintViolation<Account>> validatedSet = validator.validate(account);

        Iterator<ConstraintViolation<Account>> validatedIterator = validatedSet.iterator();
        ConstraintViolation<Account> constraintViolation = validatedIterator.next();

        // then
        Assertions.assertThat(validatedSet).isNotEmpty();

        Assertions.assertThat(constraintViolation.getMessage())
                .isEqualTo("영문자+숫자, 8자 이상 20자 이내이여야 합니다.");
    }

    @ParameterizedTest
    @ValueSource(strings = {"abcdefgh", "12345678"})
    @DisplayName("계정 password가 영문+숫자 조합이 아니라면 위반된 검증사항이 존재한다.")
    void accountPasswordPatternValidationFail(String inputPassword) {
        // given
        Account account = Account.builder()
                .email("goatodo@example.com")
                .password(inputPassword)
                .build();

        // when
        Set<ConstraintViolation<Account>> validatedSet = validator.validate(account);

        Iterator<ConstraintViolation<Account>> validatedIterator = validatedSet.iterator();
        ConstraintViolation<Account> constraintViolation = validatedIterator.next();

        // then
        Assertions.assertThat(validatedSet).isNotEmpty();

        Assertions.assertThat(constraintViolation.getMessage())
                .isEqualTo("영문자+숫자, 8자 이상 20자 이내이여야 합니다.");
    }

    @ParameterizedTest
    @ValueSource(strings = {"abcd1234", "abcdefghij1234567890", "password1234", "Password1234", "q1w2e3r4!@"})
    @DisplayName("계정 password가 영문 + 숫자 조합이고 8자 이상 20자 이내면 위반된 검증사항이 존재하지 않는다.")
    void accountPasswordPatternValidationSuccess(String inputPassword) {
        // given
        Account account = Account.builder()
                .email("goatodo@example.com")
                .password(inputPassword)
                .build();

        // when
        Set<ConstraintViolation<Account>> validatedSet = validator.validate(account);

        // then
        Assertions.assertThat(validatedSet).isEmpty();
    }
}