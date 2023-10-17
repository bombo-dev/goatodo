package com.goatodo.domain.todo;

import lombok.Getter;

@Getter
public enum Difficulty {

    VERY_EASY(2),
    EASY(5),
    NORMAL(8),
    HARD(12),
    VERY_HARD(20);

    private final int exp;

    Difficulty(int exp) {
        this.exp = exp;
    }
}
