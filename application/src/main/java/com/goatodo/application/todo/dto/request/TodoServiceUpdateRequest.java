package com.goatodo.application.todo.dto.request;

import com.goatodo.domain.todo.Difficulty;
import lombok.Builder;

@Builder
public record TodoServiceUpdateRequest(
        Long userId,
        Long tagId,
        String title,
        String description,
        Difficulty difficulty
) {
}
