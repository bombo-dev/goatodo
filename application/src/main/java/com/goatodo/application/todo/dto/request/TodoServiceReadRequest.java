package com.goatodo.application.todo.dto.request;

import lombok.Builder;

@Builder
public record TodoServiceReadRequest(
        Long memberId
) {
}
