package com.goatodo.domain.user;

import com.goatodo.common.error.ErrorCode;
import com.goatodo.common.exception.DuplicateException;
import com.goatodo.common.exception.RoleException;
import com.goatodo.domain.base.BaseEntity;
import com.goatodo.domain.user.exception.InvalidEmailOrPasswordException;
import jakarta.persistence.*;
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

    @ManyToOne
    @JoinColumn(name = "occupation_id", nullable = false)
    private Occupation occupation;

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
    @Column(name = "role", nullable = false)
    private Role role;

    @Builder
    public User(Level level,
                Occupation occupation,
                SlackInfo slackInfo,
                Account account,
                String nickname,
                Role role) {
        requireNonNull(level);
        requireNonNull(occupation);
        requireNonNull(account);
        requireNonNull(nickname);
        requireNonNull(role);
        this.level = level;
        this.occupation = occupation;
        this.slackInfo = slackInfo;
        this.account = account;
        this.nickname = nickname;
        this.experience = 0;
        this.role = role;
    }

    public static User createUser(
            Level level,
            Occupation occupation,
            SlackInfo slackInfo,
            Account account,
            String nickname,
            Role role
    ) {
        return User.builder()
                .level(level)
                .occupation(occupation)
                .account(account)
                .nickname(nickname)
                .slackInfo(slackInfo)
                .role(role)
                .build();
    }

    public void changePassword(String email, String password) {
        validSameEmail(email);
        validDuplicatePassword(password);
        account = new Account(email, password);
    }

    public void changeProfile(String nickname) {
        this.nickname = nickname;
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
