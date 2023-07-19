package com.bombo.goatodo.domain.member;

import com.bombo.goatodo.util.RegexPattern;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.Pattern;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Objects;

@Embeddable
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
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

    public boolean equalsEmail(String email) {
        return this.email.equals(email);
    }

    public boolean equalsPassword(String password) {
        return this.password.equals(password);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Account account = (Account) o;
        return Objects.equals(getEmail(), account.getEmail()) && Objects.equals(getPassword(), account.getPassword());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getEmail(), getPassword());
    }
}
