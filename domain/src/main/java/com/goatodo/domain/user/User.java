package com.goatodo.domain.user;

import com.goatodo.common.error.ErrorCode;
import com.goatodo.common.exception.DuplicateException;
import com.goatodo.common.exception.RoleException;
import com.goatodo.domain.base.BaseEntity;
import com.goatodo.domain.user.exception.InvalidEmailOrPasswordException;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static java.util.Objects.requireNonNull;


@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "users")
@Entity
public class User extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "level_id", nullable = false)
    private Level level;

    @OneToOne(optional = true)
    @JoinColumn(name = "slack_info_id")
    private SlackInfo slackInfo;

    @NotNull(message = "회원의 계정은 null 일 수 없습니다.")
    @Embedded
    private Account account;

    @Column(name = "experience", nullable = false)
    private Integer experience;

    @Column(name = "nickname", length = 15, nullable = false, unique = true)
    private String nickname;

    @Enumerated(EnumType.STRING)
    @Column(name = "occupation", nullable = false)
    private Occupation occupation;

    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false)
    private Role role;

    @Builder
    public User(@NotNull Account account,
                @NotBlank String nickname,
                @NotNull Occupation occupation,
                @NotNull Role role) {
        requireNonNull(account);
        requireNonNull(nickname);
        requireNonNull(occupation);
        requireNonNull(role);
        this.account = account;
        this.nickname = nickname;
        this.occupation = occupation;
        this.role = role;
        this.experience = 0;
    }

    public void changePassword(String email, String password) {
        validSameEmail(email);
        validDuplicatePassword(password);
        account = new Account(email, password);
    }

    public void changeProfile(String nickname, Occupation occupation) {
        this.nickname = nickname;
        this.occupation = occupation;
    }

    public void interLockSlack(SlackInfo slackInfo) {
        this.slackInfo = slackInfo;
    }

    public void validSameEmail(String email) {
        if (!account.equalsEmail(email)) {
            throw new RoleException(ErrorCode.EDIT_REQUEST_IS_FORBIDDEN);
        }
    }

    public void validDuplicatePassword(String password) {
        if (account.equalsPassword(password)) {
            throw new DuplicateException(ErrorCode.MEMBER_DUPLICATE_PASSWORD);
        }
    }

    public void validLoginInfo(Account account) {
        if (this.account.isCorrect(account)) {
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
