package com.goatodo.domain.user;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;

import static java.util.Objects.requireNonNull;

@Embeddable
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode
public class Account {

    @Column(name = "email", length = 50, nullable = false, unique = true)
    private String email;

    @Column(name = "password", length = 20, nullable = false)
    private String password;

    @Builder
    public Account(String email, String password) {
        requireNonNull(email);
        requireNonNull(password);
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
