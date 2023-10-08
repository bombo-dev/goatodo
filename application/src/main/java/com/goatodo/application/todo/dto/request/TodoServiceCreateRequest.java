package com.goatodo.application.todo.dto.request;

import com.goatodo.domain.todo.Difficulty;
import lombok.Builder;

@Builder
public record TodoServiceCreateRequest(
        Long userId,
        Long tagId,
        String title,
        String description,
        Difficulty difficulty
) {


}
