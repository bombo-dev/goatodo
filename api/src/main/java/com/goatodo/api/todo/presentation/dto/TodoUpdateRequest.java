package com.goatodo.api.todo.presentation.dto;

import com.goatodo.domain.todo.Difficulty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record TodoUpdateRequest(
        @NotNull(message = "memberId는 null 일 수 없습니다.")
        Long memberId,

        @NotNull(message = "tagId는 null 일 수 없습니다.")
        Long tagId,

        @NotBlank(message = "Todo 작성 시 제목은 공백이거나 null 이면 안됩니다.")
        @Size(max = 20, message = "Todo의 제목의 길이는 20자를 초과 할 수 없습니다.")
        String title,

        @Size(max = 50, message = "Todo의 설명은 50자를 초과 할 수 없습니다.")
        String description,

        @NotNull(message = "난이도는 null 일 수 없습니다.")
        Difficulty difficulty
) {
}
