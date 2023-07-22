package com.bombo.goatodo.domain.todo.controller.dto;

public record TodoReadRequest(
        Long todoId,
        Long memberId
) {
}
