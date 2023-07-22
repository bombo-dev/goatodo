package com.bombo.goatodo.domain.todo;

public enum CompleteStatus {
    READY(0),
    PROGRESS(50),
    COMPLETE(100);

    private final int score;

    CompleteStatus(int score) {
        this.score = score;
    }
}
