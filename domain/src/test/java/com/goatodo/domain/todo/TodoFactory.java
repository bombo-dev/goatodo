package com.goatodo.domain.todo;

import com.goatodo.domain.tag.Tag;

public final class TodoFactory {

    private TodoFactory() {

    }

    public static Tag tag() {
        return Tag.builder()
                .userId(1L)
                .name("태그")
                .build();
    }

    public static Tag tag(String name) {
        return Tag.builder()
                .userId(1L)
                .name(name)
                .build();
    }

    public static Todo todo() {
        return Todo.builder()
                .userId(1L)
                .tag(tag())
                .title("제목")
                .description("내용")
                .completeStatus(CompleteStatus.READY)
                .difficulty(Difficulty.EASY)
                .build();
    }

    public static Todo todo(CompleteStatus completeStatus) {
        return Todo.builder()
                .userId(1L)
                .tag(tag())
                .title("제목")
                .description("내용")
                .completeStatus(completeStatus)
                .difficulty(Difficulty.EASY)
                .build();
    }

    public static Todo todo(Difficulty difficulty) {
        return Todo.builder()
                .userId(1L)
                .tag(tag())
                .title("제목")
                .description("내용")
                .completeStatus(CompleteStatus.READY)
                .difficulty(difficulty)
                .build();
    }
}
