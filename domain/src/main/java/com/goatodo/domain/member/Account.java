package com.goatodo.domain.member;

import com.goatodo.common.util.RegexPattern;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.Pattern;
import lombok.*;

@Embeddable
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode
public class Account {

    @Pattern(regexp = RegexPattern.EMAIL_REGEX,
            message = "적절한 이메일 양식대로 입력해주세요. ex) goatodo@example.com")
    @Column(name = "email", length = 50, nullable = false, unique = true)
    private String email;

    @Pattern(regexp = RegexPattern.PASSWORD_REGEX, message = "영문자+숫자, 8자 이상 20자 이내이여야 합니다.")
    @Column(name = "password", length = 20, nullable = false)
    private String password;

    @Builder
    public Account(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public boolean isCorrect(Account account) {
        return this.equals(account);
    }

    public boolean equalsEmail(String email) {
        return this.email.equals(email);
    }

    public boolean equalsPassword(String password) {
        return this.password.equals(password);
    }
}
