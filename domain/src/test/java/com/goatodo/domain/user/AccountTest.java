package com.goatodo.domain.user;

import com.goatodo.domain.user.exception.InvalidEmailOrPasswordException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class AccountTest {

    @Test
    @DisplayName("회원 계정 정보의 아이디만 일치하고 비밀번호가 일치하지 않으면 같은 계정이 아니다.")
    void notSameMemberWithDifferentEmailAndPasswordTest() {
        // given
        Account accountA = UserFactory.account("email@naver.com", "password1234");
        Account accountB = UserFactory.account("email@naver.com", "password123");

        User user = UserFactory.user(accountA);

        // when & then
        Assertions.assertThatThrownBy(() -> {
            user.validMatchPassword(accountB);
        }).isInstanceOf(InvalidEmailOrPasswordException.class);
    }

    @Test
    @DisplayName("회원 계정 정보의 아이디와 비밀번호가 일치하면 같은 회원이다.")
    void SameMemberTest() {
        // given
        Account accountA = UserFactory.account("email@naver.com", "password1234");
        User user = UserFactory.user(accountA);

        // when & then
        Assertions.assertThatCode(() -> {
            user.validMatchPassword(accountA);
        }).doesNotThrowAnyException();
    }
}
