package com.goatodo.domain.user;

import com.goatodo.common.error.ErrorCode;
import com.goatodo.common.exception.DuplicateException;
import com.goatodo.common.exception.RoleException;
import com.goatodo.domain.base.BaseEntity;
import com.goatodo.domain.occupation.Occupation;
import com.goatodo.domain.slackInfo.SlackInfo;
import jakarta.persistence.*;
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

    @Column(name = "level_id", nullable = false)
    private Long levelId;

    @ManyToOne
    @JoinColumn(name = "occupation_id", nullable = false)
    private Occupation occupation;

    @OneToOne(optional = true)
    @JoinColumn(name = "slack_info_id")
    private SlackInfo slackInfo;

    @Embedded
    private Account account;

    @Column(name = "experience", nullable = false)
    private Integer experience;

    @Column(name = "nickname", length = 15, nullable = false, unique = true)
    private String nickname;

    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false)
    private Role role;

    @Transient
    private LevelStatus levelStatus;

    @Builder
    private User(Long levelId,
                 Occupation occupation,
                 SlackInfo slackInfo,
                 Account account,
                 String nickname,
                 Role role) {
        requireNonNull(levelId);
        requireNonNull(occupation);
        requireNonNull(account);
        requireNonNull(nickname);
        requireNonNull(role);
        this.levelId = levelId;
        this.occupation = occupation;
        this.slackInfo = slackInfo;
        this.account = account;
        this.nickname = nickname;
        this.experience = 0;
        this.role = role;
    }

    public static User createUser(
            Long levelId,
            Occupation occupation,
            SlackInfo slackInfo,
            Account account,
            String nickname,
            Role role
    ) {
        return User.builder()
                .levelId(levelId)
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

    public void requireExp(int experience, int requiredExperience) {
        this.experience += experience;

        if (this.experience >= requiredExperience) {
            levelStatus = LevelStatus.LEVEL_UP;
            this.experience -= requiredExperience;
        }
    }

    public void rollbackExp(int experience, int preExperience) {
        this.experience -= experience;

        if (this.experience < 0) {
            levelStatus = LevelStatus.LEVEL_DOWN;
            this.experience += preExperience;
        }
    }

    public void changeLevel(Long levelId) {
        this.levelId = levelId;
        levelStatus = LevelStatus.NORMAL;
    }

    public boolean isLevelUp() {
        return levelStatus.isLevelUp();
    }

    public boolean isLevelDown() {
        return levelStatus.isLevelDown();
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

    public void validMatchPassword(Account account) {
        if (!this.account.isCorrect(account)) {
            throw new IllegalArgumentException(ErrorCode.MEMBER_LOGIN_FAILED + "- : " + account.toString());
        }
    }
}