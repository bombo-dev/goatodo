package com.goatodo.api.todo.presentation.dto;

import com.goatodo.domain.todo.CompleteStatus;
import jakarta.validation.constraints.NotNull;

public record TodoCompleteStatusUpdateRequest(
        @NotNull(message = "todo의 id가 null 일 수 없습니다.")
        Long todoId,

        @NotNull(message = "member의 id가 null 일 수 없습니다.")
        Long memberId,

        @NotNull(message = "todo 진행 상태가 null 일 수 없습니다.")
        CompleteStatus completeStatus
) {
}
