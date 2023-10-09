package com.goatodo.domain.user;

import java.time.LocalTime;

public final class UserFactory {

    private UserFactory() {

    }

    public static Account account() {
        return Account.builder()
                .email("Test@naver.com")
                .password("password1234!")
                .build();
    }

    public static Account account(String email, String password) {
        return Account.builder()
                .email(email)
                .password(password)
                .build();
    }

    public static Level level() {
        return Level.builder()
                .level(1)
                .preExperience(0)
                .requiredExperience(10)
                .build();
    }

    public static Level level(int level, int preExperience, int requiredExperience) {
        return Level.builder()
                .level(level)
                .preExperience(preExperience)
                .requiredExperience(requiredExperience)
                .build();
    }

    public static SlackInfo slackInfo() {
        return SlackInfo.builder()
                .slackEmail("slack@google.com")
                .startAlarmTime(LocalTime.of(9, 0))
                .endAlarmTime(LocalTime.of(21, 0))
                .build();
    }

    public static Occupation occupation() {
        return Occupation.builder()
                .name("학생")
                .build();
    }

    public static Occupation occupation(String occupationName) {
        return Occupation.builder()
                .name(occupationName)
                .build();
    }

    public static User user() {
        return User.builder()
                .account(account())
                .level(level())
                .slackInfo(slackInfo())
                .occupation(occupation())
                .nickname("닉네임")
                .role(Role.NORMAL)
                .build();
    }

    public static User user(Level level) {
        return User.builder()
                .account(account())
                .level(level)
                .slackInfo(slackInfo())
                .occupation(occupation())
                .nickname("닉네임")
                .role(Role.NORMAL)
                .build();
    }

    public static User user(Account account) {
        return User.builder()
                .account(account)
                .level(level())
                .slackInfo(slackInfo())
                .occupation(occupation())
                .nickname("닉네임")
                .role(Role.NORMAL)
                .build();
    }
}
