package com.goatodo.domain.member;

import com.goatodo.common.error.ErrorCode;
import com.goatodo.common.exception.DuplicateException;
import com.goatodo.common.exception.RoleException;
import com.goatodo.domain.base.BaseEntity;
import com.goatodo.domain.member.exception.InvalidEmailOrPasswordException;
import jakarta.persistence.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
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

    @NotNull(message = "회원의 계정은 null 일 수 없습니다.")
    @Valid
    @Embedded
    private Account account;

    @Valid
    @Embedded
    private SlackInfo slackInfo;

    @NotBlank(message = "회원의 닉네임은 null 이거나 공백 일 수 없습니다.")
    @Size(min = 2, max = 15, message = "닉네임은 2글자 이상 15자 이내여야 합니다.")
    @Column(name = "nickname", length = 15, nullable = false, unique = true)
    private String nickname;

    @NotNull(message = "회원의 직업은 null 일 수 없습니다.")
    @Enumerated(EnumType.STRING)
    @Column(name = "occupation", nullable = false)
    private Occupation occupation;

    @NotNull(message = "회원의 역할은 null 일 수 없습니다.")
    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false)
    private Role role;

    @Builder
    public Member(@NotNull Account account,
                  @NotBlank String nickname,
                  @NotNull Occupation occupation,
                  @NotNull Role role) {
        this.account = account;
        this.nickname = nickname;
        this.occupation = occupation;
        this.role = role;
    }

    public void changePassword(String email, String password) {
        validSameEmail(email);
        validSamePassword(password);
        this.account = new Account(email, password);
    }

    public void changeProfile(String nickname, Occupation occupation) {
        this.nickname = nickname;
        this.occupation = occupation;
    }

    public void interLockSlack(SlackInfo slackInfo) {
        this.slackInfo = slackInfo;
    }

    public void validSameEmail(String email) {
        if (!this.account.equalsEmail(email)) {
            throw new RoleException(ErrorCode.EDIT_REQUEST_IS_FORBIDDEN);
        }
    }

    public void validSamePassword(String password) {
        if (this.account.equalsPassword(password)) {
            throw new DuplicateException(ErrorCode.MEMBER_DUPLICATE_PASSWORD);
        }
    }

    public void validSameMember(Account account) {
        if (!this.account.equals(account)) {
            throw new InvalidEmailOrPasswordException(ErrorCode.MEMBER_LOGIN_FAILED);
        }
    }

    public boolean isAdmin() {
        return role.isAdmin();
    }

    public boolean isNormal() {
        return role.isNormal();
    }
}
