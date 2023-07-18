package com.bombo.goatodo.util;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.regex.Pattern;

class RegexPatternTest {

    @ParameterizedTest
    @ValueSource(strings = {"abc1234", "abcdefghijk1234567890"})
    @DisplayName("비밀번호 숫자의 범위가 8자미만 20자 초과면 정규식이 일치하지 않는다.")
    void passwordExOutOfRangeLength(String input) {
        // given

        // when
        boolean isMatch = Pattern.compile(RegexPattern.PASSWORD_REGEX)
                .matcher(input)
                .matches();

        // then
        Assertions.assertThat(isMatch).isFalse();
    }

    @Test
    @DisplayName("비밀번호가 영문만 있으면 정규식이 일치하지 않는다.")
    void passwordExWithOnlyAlpha() {
        // given
        String input = "password";

        // when
        boolean isMatch = Pattern.compile(RegexPattern.PASSWORD_REGEX)
                .matcher(input)
                .matches();

        // then
        Assertions.assertThat(isMatch).isFalse();
    }

    @Test
    @DisplayName("비밀번호가 숫자만 있으면 정규식이 일치하지 않는다.")
    void passwordExWithOnlyNumeric() {
        // given
        String input = "123456789";

        // when
        boolean isMatch = Pattern.compile(RegexPattern.PASSWORD_REGEX)
                .matcher(input)
                .matches();

        // then
        Assertions.assertThat(isMatch).isFalse();
    }

    @ParameterizedTest
    @ValueSource(strings = {"abcd1234", "abcdefghij1234567890", "password1234", "Password1234", "q1w2e3r4!@"})
    @DisplayName("비밀번호가 영문자 + 숫자와 조합과 8자이상 20자 이내면 정규식이 통과한다.")
    void correctPasswordTest(String input) {
        // given

        // when
        boolean isMatch = Pattern.compile(RegexPattern.PASSWORD_REGEX)
                .matcher(input)
                .matches();

        // then
        Assertions.assertThat(isMatch).isTrue();
    }
}