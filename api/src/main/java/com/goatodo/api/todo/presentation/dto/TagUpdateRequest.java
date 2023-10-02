package com.goatodo.api.todo.presentation.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record TagUpdateRequest(
        @NotNull(message = "회원 id는 null 일 수 없습니다.")
        Long memberId,

        @NotBlank(message = "카테고리는 공백 일 수 없습니다.")
        @Size(max = 20, message = "카테고리는 20자 이내여야 합니다.")
        String name
) {
}
