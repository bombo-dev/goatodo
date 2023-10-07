package com.goatodo.domain.user;

import com.goatodo.domain.base.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Objects;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "user_affiliations")
@Entity
public class UserAffiliations extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "affiliation_id")
    private Affiliation affiliation;

    public UserAffiliations(User user, Affiliation affiliation) {
        Objects.requireNonNull(user);
        Objects.requireNonNull(affiliation);
        this.user = user;
        this.affiliation = affiliation;
    }
}
