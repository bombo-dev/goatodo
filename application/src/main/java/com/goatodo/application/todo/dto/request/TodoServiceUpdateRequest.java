package com.goatodo.application.todo.dto.request;

import lombok.Builder;

@Builder
public record TodoServiceUpdateRequest(
        Long memberId,
        Long tagId,
        String title,
        String description
) {
}
