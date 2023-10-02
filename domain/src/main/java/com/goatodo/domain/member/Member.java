package com.goatodo.domain.member;

import com.goatodo.domain.base.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Embedded
    private Account account;

    @Embedded
    private SlackInfo slackInfo;

    @Column(name = "nickname", length = 15, nullable = false, unique = true)
    private String nickname;

    @Enumerated(EnumType.STRING)
    @Column(name = "occupation", nullable = false)
    private Occupation occupation;

    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false)
    private Role role;

    @Builder
    public Member(Account account,
                  String nickname,
                  Occupation occupation,
                  Role role) {
        this.account = account;
        this.nickname = nickname;
        this.occupation = occupation;
        this.role = role;
    }

    public void changePassword(String password) {
        this.account = new Account(this.account.getEmail(), password);
    }

    public void changeProfile(String nickname, Occupation occupation) {
        this.nickname = nickname;
        this.occupation = occupation;
    }

    public void interLockSlack(SlackInfo slackInfo) {
        this.slackInfo = slackInfo;
    }

    public boolean isSameEmail(String email) {
        return this.account.equalsEmail(email);
    }

    public boolean isSamePassword(String password) {
        return this.account.equalsPassword(password);
    }

    public boolean isSameMember(Account account) {
        return this.account.equals(account);
    }

    public boolean isAdmin() {
        return this.role == Role.ADMIN;
    }

    public boolean isNormal() {
        return this.role == Role.NORMAL;
    }
}
