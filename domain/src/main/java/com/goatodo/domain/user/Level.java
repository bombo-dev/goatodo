package com.goatodo.domain.user;

import com.goatodo.domain.base.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Objects;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "levels")
@Entity
public class Level extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "level", nullable = false)
    private Integer level;

    @Column(name = "required_experience", nullable = false)
    private Integer requiredExperience;

    @Builder
    public Level(Integer level, Integer requiredExperience) {
        Objects.requireNonNull(level);
        Objects.requireNonNull(requiredExperience);
        this.level = level;
        this.requiredExperience = requiredExperience;
    }
}