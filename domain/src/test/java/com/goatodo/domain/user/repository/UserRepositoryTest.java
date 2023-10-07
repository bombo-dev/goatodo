package com.goatodo.domain.user.repository;

import com.goatodo.domain.config.RepositoryTestConfiguration;
import com.goatodo.domain.user.Account;
import com.goatodo.domain.user.OccupationType;
import com.goatodo.domain.user.Role;
import com.goatodo.domain.user.User;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ContextConfiguration;

import java.util.List;
import java.util.Optional;

@DataJpaTest
@ContextConfiguration(classes = RepositoryTestConfiguration.class)
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    @DisplayName("회원을 생성합니다.")
    void memberCreateSuccess() {
        // given
        Account account = Account.builder()
                .email("goatodo@email.com")
                .password("password1234")
                .build();

        User user = User.builder()
                .account(account)
                .nickname("고투두")
                .occupation(OccupationType.GENERAL)
                .role(Role.NORMAL)
                .build();

        // when
        User savedUser = userRepository.save(user);
        User findUser = userRepository.findById(savedUser.getId()).get();

        // then
        Assertions.assertThat(savedUser).isEqualTo(findUser);
    }

    @Test
    @DisplayName("회원을 조회 할 수 있다.")
    void findMemberTest() {
        // given
        Account account = Account.builder()
                .email("goatodo@email.com")
                .password("password1234")
                .build();

        User user = User.builder()
                .account(account)
                .nickname("고투두")
                .occupation(OccupationType.GENERAL)
                .role(Role.NORMAL)
                .build();

        User savedUser = userRepository.save(user);

        // when
        User findUser = userRepository.findById(savedUser.getId()).get();

        // then
        Assertions.assertThat(savedUser).isEqualTo(findUser);
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


        User userA = User.builder()
                .account(accountA)
                .nickname("고투두")
                .occupation(OccupationType.GENERAL)
                .role(Role.NORMAL)
                .build();

        User userB = User.builder()
                .account(accountB)
                .nickname("고투두2")
                .occupation(OccupationType.GENERAL)
                .role(Role.NORMAL)
                .build();

        userRepository.save(userA);
        userRepository.save(userB);

        // when
        List<User> findUsers = userRepository.findAll();

        // then
        Assertions.assertThat(findUsers).hasSize(2);
    }

    @Test
    @DisplayName("회원을 삭제 할 수 있다.")
    void deleteMember() {
        // given
        Account account = Account.builder()
                .email("goatodo@email.com")
                .password("password1234")
                .build();

        User user = User.builder()
                .account(account)
                .nickname("고투두")
                .occupation(OccupationType.GENERAL)
                .role(Role.NORMAL)
                .build();

        User savedUser = userRepository.save(user);

        // when
        userRepository.delete(savedUser);
        List<User> findUsers = userRepository.findAll();

        // then
        Assertions.assertThat(findUsers).isEmpty();
    }

    @Test
    @DisplayName("닉네임으로 회원을 조회 할 수 있다.")
    void findByNicknameTest() {
        // given
        Account account = Account.builder()
                .email("goatodo@email.com")
                .password("password1234")
                .build();

        User user = User.builder()
                .account(account)
                .nickname("고투두")
                .occupation(OccupationType.GENERAL)
                .role(Role.NORMAL)
                .build();

        User savedUser = userRepository.save(user);

        // when
        Optional<User> findMember = userRepository.findByNickname(savedUser.getNickname());

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

        User user = User.builder()
                .account(account)
                .nickname("고투두")
                .occupation(OccupationType.GENERAL)
                .role(Role.NORMAL)
                .build();

        User savedUser = userRepository.save(user);

        // when
        Optional<User> findMember = userRepository.findByAccount_Email(savedUser.getAccount().getEmail());

        // then
        Assertions.assertThat(findMember).isNotEmpty();
    }
}