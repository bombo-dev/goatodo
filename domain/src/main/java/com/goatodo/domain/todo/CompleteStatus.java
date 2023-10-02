package com.goatodo.domain.todo;

public enum CompleteStatus {
    READY(0, "시작 전"),
    PROGRESS(50, "진행 중"),
    COMPLETE(100, "완료");

    private final int score;
    private final String statusName;

    CompleteStatus(int score, String statusName) {
        this.score = score;
        this.statusName = statusName;
    }

    public String getStatusName() {
        return statusName;
    }

    public int getScore() {
        return score;
    }
}
