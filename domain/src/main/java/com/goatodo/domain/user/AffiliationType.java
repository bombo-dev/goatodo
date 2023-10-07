package com.goatodo.domain.user;

import com.goatodo.domain.base.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Objects;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "affiliation_type")
@Entity
public class AffiliationType extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String type;

    public AffiliationType(String type) {
        Objects.requireNonNull(type);
        this.type = type;
    }
}
