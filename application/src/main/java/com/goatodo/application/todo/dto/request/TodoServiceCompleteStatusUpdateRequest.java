package com.goatodo.application.todo.dto.request;

import com.goatodo.domain.todo.CompleteStatus;
import lombok.Builder;

@Builder
public record TodoServiceCompleteStatusUpdateRequest(
        Long memberId,
        CompleteStatus completeStatus
) {
}
