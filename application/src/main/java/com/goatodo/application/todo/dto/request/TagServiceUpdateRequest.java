package com.goatodo.application.todo.dto.request;

import lombok.Builder;

@Builder
public record TagServiceUpdateRequest(
        Long userId,
        String name
) {
}
