package com.bombo.goatodo.domain.member;

import com.bombo.goatodo.common.BaseEntity;
import jakarta.persistence.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "회원의 계정은 null 일 수 없습니다.")
    @Valid
    @Embedded
    private Account account;

    @NotBlank(message = "회원의 닉네임은 null 이거나 공백 일 수 없습니다.")
    @Length(min = 2, max = 15, message = "닉네임은 2글자 이상 15자 이내여야 합니다.")
    @Column(name = "nickname", length = 15, nullable = false)
    private String nickname;

    @NotNull(message = "회원의 직업은 null 일 수 없습니다.")
    @Enumerated(EnumType.STRING)
    @Column(name = "occupation", nullable = false)
    private Occupation occupation;

    @Builder
    public Member(@NotNull Account account,
                  @NotBlank String nickname,
                  @NotNull Occupation occupation) {
        this.account = account;
        this.nickname = nickname;
        this.occupation = occupation;
    }

    public boolean isSameMember(Account account) {
        return this.account.equals(account);
    }
}
