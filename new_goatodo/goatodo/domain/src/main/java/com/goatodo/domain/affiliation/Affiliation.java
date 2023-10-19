package com.goatodo.domain.affiliation;

import com.goatodo.domain.base.BaseEntity;
import com.goatodo.domain.occupation.Occupation;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Objects;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "affiliations")
@Entity
public class Affiliation extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "occupation_id", nullable = false)
    private Occupation occupation;

    @ManyToOne
    @JoinColumn(name = "affiliation_type_id", nullable = false)
    private AffiliationType affiliationType;

    @Column(name = "name", length = 30, nullable = false)
    private String name;

    @Column(name = "years", nullable = false)
    private int years;

    @Builder
    private Affiliation(Occupation occupation, AffiliationType affiliationType, String name, int years) {
        Objects.requireNonNull(occupation);
        Objects.requireNonNull(affiliationType);
        Objects.requireNonNull(name);
        this.occupation = occupation;
        this.affiliationType = affiliationType;
        this.name = name;
        this.years = years;
    }
}
