package com.goatodo.domain.user;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum LevelStatus {
    NORMAL("평상 시"),
    LEVEL_UP("레벨 업"),
    LEVEL_DOWN("레벨 다운");

    private final String text;

    public boolean isLevelUp() {
        return this == LEVEL_UP;
    }

    public boolean isLevelDown() {
        return this == LEVEL_DOWN;
    }
}
