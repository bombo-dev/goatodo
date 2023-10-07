package com.goatodo.domain.todo;

import lombok.Getter;

@Getter
public enum Difficulty {

    VERY_EASY(5),
    EASY(7),
    NORMAL(10),
    HARD(13),
    VERY_HARD(20);

    private final int exp;

    Difficulty(int exp) {
        this.exp = exp;
    }
}
